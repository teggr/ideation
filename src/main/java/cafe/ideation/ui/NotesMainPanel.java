package cafe.ideation.ui;

import cafe.ideation.model.Note;
import cafe.ideation.service.NoteService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotesMainPanel extends JPanel {
    private final NoteService noteService;
    private final JList<Note> notesList;
    private final DefaultListModel<Note> listModel;
    private final JTabbedPane tabbedPane;

    public NotesMainPanel(NoteService noteService) {
        this.noteService = noteService;
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        notesList = new JList<>(listModel);
        notesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabbedPane = new JTabbedPane();

        // Toolbar setup
        JToolBar toolBar = new JToolBar();
        JButton createNoteButton = new JButton("Create Note");
        createNoteButton.addActionListener(e -> {
            Note newNote = noteService.createNote();
            listModel.addElement(newNote);
            notesList.setSelectedValue(newNote, true);
            openNoteTab(newNote);
        });
        toolBar.add(createNoteButton);
        add(toolBar, BorderLayout.PAGE_START);

        // Load notes into list
        List<Note> notes = noteService.listNotes();
        for (Note note : notes) {
            listModel.addElement(note);
        }

        // Select and show the first note if available
        if (!notes.isEmpty()) {
            notesList.setSelectedIndex(0);
            openNoteTab(notes.get(0));
        }

        notesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                for (Note selectedNote : notesList.getSelectedValuesList()) {
                    openNoteTab(selectedNote);
                }
            }
        });

        notesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(true);
                if (isSelected) {
                    panel.setBackground(list.getSelectionBackground());
                } else {
                    panel.setBackground(list.getBackground());
                }
                if (value instanceof Note note) {
                    JLabel titleLabel = new JLabel(note.getTitle().isEmpty() ? "Untitled" : note.getTitle());
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
                    LocalDateTime mostRecent = note.getDateUpdated().isAfter(note.getDateCreated()) ? note.getDateUpdated() : note.getDateCreated();
                    String dateText;
                    if (mostRecent.toLocalDate().equals(LocalDate.now())) {
                        dateText = mostRecent.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    } else {
                        dateText = mostRecent.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                    JLabel dateLabel = new JLabel(dateText);
                    dateLabel.setFont(dateLabel.getFont().deriveFont(Font.PLAIN, 10f));
                    panel.add(titleLabel, BorderLayout.NORTH);
                    panel.add(dateLabel, BorderLayout.SOUTH);
                }
                return panel;
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(notesList), tabbedPane);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);
    }

    private void openNoteTab(Note note) {
        String tabTitle = note.getTitle().isEmpty() ? "Untitled" : note.getTitle();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(tabTitle)) {
                tabbedPane.setSelectedIndex(i);
                return;
            }
        }
        NotePanel notePanel = new NotePanel(note, noteService);
        JButton closeButton = new JButton("Close");
        JPanel tabHeader = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(tabTitle);
        tabHeader.add(titleLabel, BorderLayout.CENTER);
        tabHeader.add(closeButton, BorderLayout.EAST);
        closeButton.addActionListener(e -> tabbedPane.remove(notePanel));
        tabbedPane.addTab(tabTitle, notePanel);
        int tabIdx = tabbedPane.getTabCount() - 1;
        tabbedPane.setTabComponentAt(tabIdx, tabHeader);
        tabbedPane.setSelectedComponent(notePanel);

        // Update tab title if note title changes after save
        notePanel.setOnTitleChanged(newTitle -> {
            titleLabel.setText(newTitle);
            tabbedPane.setTitleAt(tabIdx, newTitle);
        });
        // Update the notes list when the note is saved (title/date may have changed)
        notePanel.setOnNoteSaved(() -> {
            int idx = listModel.indexOf(note);
            if (idx >= 0) {
                // Fire contentsChanged to update renderer
                listModel.set(idx, note);
            }
        });
    }

    public void showNote(Note newNote) {
        listModel.addElement(newNote);
        notesList.setSelectedValue(newNote, true);
        openNoteTab(newNote);
    }

}
