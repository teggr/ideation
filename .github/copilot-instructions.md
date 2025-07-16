# Copilot Instructions for the Ideation Project

## Project Overview
- **Purpose:** Desktop notes/ideas app using Java, Spring Boot, and Swing with a modern UI (FlatLaf).
- **Persistence:** Notes are stored as files (metadata in `.properties`, content in `.txt`/`.dat`) in a user data directory managed by `AppDataDirectoryService`.
- **Architecture:**
  - `NoteService`: Low-level file-based CRUD and note metadata/content management (not used directly by UI).
  - `NoteApplicationService`: Main application service for all note actions (create, edit, delete, save, list, etc). Delegates to `NoteService` and publishes events for all actions using Spring's `ApplicationEventPublisher`.
  - `AppDataDirectoryService`: Handles app data directory and data collection registration.
  - `NotesMainPanel`, `NotePanel`, `WelcomePanel`: Swing UI, event-driven, with tabbed editing, infinite scroll, and live updates. All UI components depend on `NoteApplicationService` (not `NoteService`).
  - Event-driven updates via Spring's `ApplicationEventPublisher` and `@EventListener` for cross-component communication.

## Key Patterns & Conventions
- **File-based notes:**
  - Each note: `title.properties` (metadata), `title.txt` or `title.dat` (content, based on content type).
  - Title is always the first line of content (auto-updated on save).
  - Use `NoteApplicationService.saveNote(note, newContent)` for all edits; this updates content, title, and timestamps, and fires a `NoteSavedEvent`.
- **UI/Service Integration:**
  - UI panels (`NotePanel`, `NotesMainPanel`, `WelcomePanel`) are constructed with `NoteApplicationService` injected (never use `NoteService` directly in UI).
  - `NotePanel` exposes callbacks for parent panels to update list/tabs on save/title change.
  - List model (`DefaultListModel<Note>`) is updated in-place to reflect changes after save.
  - All note actions (create, delete, edit, save) should go through `NoteApplicationService` to ensure events are published and listeners are notified.
- **Event-driven UI:**
  - Use Spring `@EventListener` to react to events published by `NoteApplicationService` (e.g., `NoteCreatedEvent`, `NoteDeletedEvent`, `NoteSavedEvent`).
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
- To add/edit a note: Use `NoteApplicationService.saveNote(note, newContent)`; UI will update list and tabs automatically and events will be published.
- To add a new data collection: Register with `AppDataDirectoryService.registerDataCollection`.
- To update the notes list after a save: Fire `listModel.set(idx, note)` in `NotesMainPanel`.

## Key Files
- `src/main/java/cafe/ideation/service/NoteService.java` (low-level, not used directly by UI)
- `src/main/java/cafe/ideation/service/NoteApplicationService.java` (main service for all note actions and events)
- `src/main/java/cafe/ideation/service/AppDataDirectoryService.java`
- `src/main/java/cafe/ideation/ui/NotesMainPanel.java`
- `src/main/java/cafe/ideation/ui/NotePanel.java`
- `src/main/java/cafe/ideation/model/Note.java`

## Special Considerations
- **Filename safety:** Titles are used as filenames; sanitize or randomize if empty.
- **No database:** All persistence is file-based for simplicity and portability.
- **UI/Service decoupling:** Use callbacks/events for communication, not direct references. All UI logic should use `NoteApplicationService` for note actions to ensure event-driven updates.

---
_If you add new features, follow the file-based, event-driven, and UI/service separation patterns above._
