package cafe.ideation.ui;

import cafe.ideation.model.Note;
import cafe.ideation.service.NoteService;

import javax.swing.*;
import java.awt.*;
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
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Note note) {
                    label.setText(note.getTitle().isEmpty() ? "Untitled" : note.getTitle());
                }
                return label;
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
        NotePanel notePanel = new NotePanel(note);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> tabbedPane.remove(notePanel));
        JPanel tabHeader = new JPanel(new BorderLayout());
        tabHeader.add(new JLabel(tabTitle), BorderLayout.CENTER);
        tabHeader.add(closeButton, BorderLayout.EAST);
        tabbedPane.addTab(tabTitle, notePanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabHeader);
        tabbedPane.setSelectedComponent(notePanel);
    }

    public void showNote(Note newNote) {
        listModel.addElement(newNote);
        notesList.setSelectedValue(newNote, true);
        openNoteTab(newNote);
    }

}
