# Copilot Instructions for the Ideation Project

## Project Overview
- **Purpose:** Desktop notes/ideas app using Java, Spring Boot, and Swing with a modern UI (FlatLaf).
- **Persistence:** Notes are stored as files (metadata in `.properties`, content in `.txt`/`.dat`) in a user data directory managed by `AppDataDirectoryService`.
- **Architecture:**
  - `NoteService`: Core logic for CRUD, file I/O, and note metadata/content management.
  - `AppDataDirectoryService`: Handles app data directory and data collection registration.
  - `NotesMainPanel`, `NotePanel`: Swing UI, event-driven, with tabbed editing, infinite scroll, and live updates.
  - Event-driven updates via Spring's `ApplicationEventPublisher` and `@EventListener`.

## Key Patterns & Conventions
- **File-based notes:**
  - Each note: `title.properties` (metadata), `title.txt` or `title.dat` (content, based on content type).
  - Title is always the first line of content (auto-updated on save).
  - Use `NoteService.saveNote(note, newContent)` for all edits; this updates content, title, and timestamps.
- **UI/Service Integration:**
  - UI panels (`NotePanel`, `NotesMainPanel`) are constructed with service dependencies injected.
  - `NotePanel` exposes callbacks for parent panels to update list/tabs on save/title change.
  - List model (`DefaultListModel<Note>`) is updated in-place to reflect changes after save.
- **Event-driven UI:**
  - Use event listeners for cross-component updates (e.g., new note creation, list refresh).
  - UI is designed for responsiveness: infinite scroll, loading indicators, and dirty tracking.
- **Modern Swing UI:**
  - FlatLaf for look and feel.
  - Maximized main window, toolbar, menu bar, and custom cell renderers for notes list.

## Developer Workflows
- **Build:** Standard Maven (`mvn clean package`).
- **Run:** Use Spring Boot (`mvn spring-boot:run` or run main class).
- **Debug:** Launch via IDE (recommended: IntelliJ IDEA) for full UI debugging.
- **No tests** (as of now) — focus is on manual UI/UX validation.

## Examples
- To add/edit a note: Use `NoteService.saveNote(note, newContent)`; UI will update list and tabs automatically.
- To add a new data collection: Register with `AppDataDirectoryService.registerDataCollection`.
- To update the notes list after a save: Fire `listModel.set(idx, note)` in `NotesMainPanel`.

## Key Files
- `src/main/java/cafe/ideation/service/NoteService.java`
- `src/main/java/cafe/ideation/service/AppDataDirectoryService.java`
- `src/main/java/cafe/ideation/ui/NotesMainPanel.java`
- `src/main/java/cafe/ideation/ui/NotePanel.java`
- `src/main/java/cafe/ideation/model/Note.java`

## Special Considerations
- **Filename safety:** Titles are used as filenames; sanitize or randomize if empty.
- **No database:** All persistence is file-based for simplicity and portability.
- **UI/Service decoupling:** Use callbacks/events for communication, not direct references.

---
_If you add new features, follow the file-based, event-driven, and UI/service separation patterns above._
