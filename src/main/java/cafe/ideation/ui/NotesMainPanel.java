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

        // Load notes into list
        List<Note> notes = noteService.listNotes();
        for (Note note : notes) {
            listModel.addElement(note);
        }

        notesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                for (Note selectedNote : notesList.getSelectedValuesList()) {
                    openNoteTab(selectedNote);
                }
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
}
