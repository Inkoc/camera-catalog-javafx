# Camera Catalog

A desktop application for managing a catalog of photographic cameras and equipment —
cameras, brands, lens compatibility, and intended use. Built with **JavaFX** and
**PostgreSQL**, following a clean layered architecture (controller → service →
repository → model) with dependency injection, the repository pattern, and an
event-driven UI.

Administrators manage the catalog (add/edit/delete cameras and lenses, attach lenses
to cameras via drag & drop, import/export the catalog as XML); regular users browse,
search, and filter.

---

## Features

- **Authentication & roles** — BCrypt-hashed passwords, `ADMIN` / `USER` roles, session management.
- **Camera & lens CRUD** — full create/read/update/delete through PostgreSQL stored procedures.
- **Drag & drop** — attach/detach lenses to a camera by dragging between lists.
- **Search & filter** — live filtering of cameras by name, type, and purpose.
- **Images** — per-camera image stored on disk, shown as a live preview.
- **XML import** — load a catalog from an XML file (JAXB), de-duplicated on import.
- **XML export** — export the full catalog (cameras with nested lenses and specs) via JAXB.
- **Action logging** — user actions (login, CRUD, import/export) are appended to an XML audit log.
- **Responsive UI** — long-running work (import/export) runs off the JavaFX thread.

---

## Tech Stack

| Area      | Technology                          |
|-----------|-------------------------------------|
| Language  | Java 21                             |
| UI        | JavaFX 21 (FXML)                    |
| Database  | PostgreSQL (stored procedures)      |
| XML       | JAXB (Jakarta XML Binding) + DOM    |
| Security  | JBCrypt                             |
| Build     | Maven (wrapper included)            |

---

## Architecture

Dependencies point inward; each layer only knows the layer below it through interfaces.

```
controller  ──▶  service (interface)  ──▶  repository (interface)  ──▶  model
    │                                              │
    │                                       PostgreSQL stored procedures
    │
    ├── ViewManager        — centralized scene/modal navigation + DI
    ├── ControllerFactory  — constructor injection for FXML controllers
    ├── ThreadManager      — background task execution (off the FX thread)
    └── EventBus           — Observer pattern: views refresh + actions are logged
```

- **Repository pattern** — every data access goes through an interface; PostgreSQL
  implementations call stored procedures with bound parameters (no string concatenation,
  no SQL injection).
- **Custom DI** — `ControllerFactory` builds the service/repository graph once and
  injects dependencies into controllers via their constructors.
- **Observer (EventBus)** — on any data change, a `DataChangedEvent` is published; open
  views refresh and the action logger records it — all from one event stream.
- **Utilities** — static, final, private-constructor helpers (`ConfigurationManager`,
  `ViewManager`, `ThreadManager`, `ImageStorage`, `TableColumnFactory`, `DialogUtils`).

---

## Project Structure

```
hr.algebra.camera
├── auth/           SessionManager, PasswordHasher
├── model/          Camera, Lens, Brand, User, enums, dto/ (JAXB DTOs)
├── repository/     interfaces/ + postgres/ (stored-procedure implementations)
├── service/        interfaces/ + business logic (CRUD, import, export, auth)
├── event/          EventBus, EventListener, DataChangedEvent
├── controller/     Login, Register, Main, Camera, Lens, Admin (+ forms) + ControllerFactory
├── exception/      custom checked/unchecked exceptions
└── utils/          ConfigurationManager, ViewManager, ThreadManager, ImageStorage, ...

src/main/resources/hr/algebra/camera/
├── view/           FXML screens
├── config/         application-config.xml  (window size + DB connection)
└── db/             ddl.sql, clear.sql, procedures.sql, seed.sql
```

---

## Database Setup

Requires a running PostgreSQL instance. Run the scripts in
`src/main/resources/hr/algebra/camera/db/` in this order:

1. `ddl.sql` — creates all tables.
2. `procedures.sql` — creates all stored procedures and functions.
3. `seed.sql` — *(optional)* inserts sample brands, lenses, and cameras.

`clear.sql` wipes all data when you need a clean slate.

---

## Configuration

Window size and the database connection are read at startup from
`src/main/resources/hr/algebra/camera/config/application-config.xml`:

```xml
<configuration>
    <window>
        <width>1280</width>
        <height>800</height>
        <title>Camera Catalog</title>
    </window>
    <database>
        <url>jdbc:postgresql://localhost:5432/camera_db</url>
        <username>your_user</username>
        <password>your_password</password>
    </database>
</configuration>
```

Update the `<database>` values to match your PostgreSQL setup.

---

## Running

Requires **JDK 21+**. Using the Maven wrapper:

```bash
./mvnw.cmd javafx:run     # Windows
./mvnw javafx:run         # macOS / Linux
```

On first launch a default administrator account is created automatically.

### Default credentials

| Username | Password | Role  |
|----------|----------|-------|
| `admin`  | `admin`  | ADMIN |

New accounts created through the registration screen are always `USER` role.

---

## XML Import / Export

The admin screen imports a catalog from an XML file and exports the current catalog.
Format (export nests each camera's lenses):

```xml
<cameraCatalog>
    <cameras>
        <camera>
            <name>Canon EOS R6</name>
            <releaseYear>2020</releaseYear>
            <megapixels>20.1</megapixels>
            <sensorType>Full-Frame CMOS</sensorType>
            <isoRange>100-102400</isoRange>
            <maxShootingSpeed>20</maxShootingSpeed>
            <price>2499.00</price>
            <cameraType>MIRRORLESS</cameraType>
            <purpose>SPORTS</purpose>
            <brand>Canon</brand>
            <lenses>
                <lens>
                    <name>Canon RF 50mm f/1.8</name>
                    <focalLength>50</focalLength>
                    <aperture>1.8</aperture>
                    <mountType>RF</mountType>
                    <price>199.00</price>
                </lens>
            </lenses>
        </camera>
    </cameras>
</cameraCatalog>
```

`cameraType` and `purpose` must match the application's enum names. Brands are resolved
by name and created on the fly if they don't exist. Imports skip cameras whose name
already exists, so the same file can be imported repeatedly without duplicates.
