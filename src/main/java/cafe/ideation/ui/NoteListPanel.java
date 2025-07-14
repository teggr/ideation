package cafe.ideation.ui;

import cafe.ideation.model.Note;
import cafe.ideation.service.NoteService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

public class NoteListPanel extends JPanel {
    
    private final NoteService noteService;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private int pageSize = 20;
    private int currentMaxIndex = 0;
    private List<Note> allNotes = new ArrayList<>();
    private final JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);

    public NoteListPanel(NoteService noteService) {
        this.noteService = noteService;
        setLayout(new BorderLayout());
        initInfiniteScroll();
    }

    private void initInfiniteScroll() {
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(listPanel);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting() && scrollPane.getVerticalScrollBar().getValue() + scrollPane.getVerticalScrollBar().getVisibleAmount() >= scrollPane.getVerticalScrollBar().getMaximum()) {
                    showLoadingIndicator();
                    SwingUtilities.invokeLater(() -> {
                        loadNextPage();
                        hideLoadingIndicator();
                    });
                }
            }
        });
        add(scrollPane, BorderLayout.CENTER);
        refreshNotes();
    }

    private void showLoadingIndicator() {
        if (loadingLabel.getParent() == null) {
            add(loadingLabel, BorderLayout.SOUTH);
            revalidate();
            repaint();
        }
    }

    private void hideLoadingIndicator() {
        remove(loadingLabel);
        revalidate();
        repaint();
    }

    public void refreshNotes() {
        listPanel.removeAll();
        allNotes = noteService.listNotes();
        currentMaxIndex = 0;
        hideLoadingIndicator();
        if (allNotes.isEmpty()) {
            listPanel.add(new JLabel("No notes found. Create your first note!", SwingConstants.CENTER));
        } else {
            loadNextPage();
        }
        revalidate();
        repaint();
    }

    private void loadNextPage() {
        int nextMax = Math.min(currentMaxIndex + pageSize, allNotes.size());
        for (int i = currentMaxIndex; i < nextMax; i++) {
            Note note = allNotes.get(i);
            listPanel.add(new JLabel(note.getTitle() + " (Created: " + note.getDateCreated() + ", Updated: " + note.getDateUpdated() + ")"));
        }
        currentMaxIndex = nextMax;
        revalidate();
        repaint();
    }
}
