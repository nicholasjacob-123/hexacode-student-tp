package com.hexacode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.naming.InvalidNameException;
import javax.swing.*;

import com.hexacode.Entry.TodoType;
import com.hexacode.exceptions.IllegalDateException;
import com.hexacode.exceptions.InvalidSubjectException;

public class EntryManagerPanel extends JPanel implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("wow such add");

        NewEntryPanel newEntry = new NewEntryPanel();

        int result = JOptionPane.showConfirmDialog(null, newEntry, "Create new entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // get field values
            Entry entry;
            try {
                String name = newEntry.getName();
                String subj = newEntry.getSubject();
                TodoType type = newEntry.getTodoType();
                LocalDateTime deadline = newEntry.getDeadline();

                if (name.equals("")) 
                    throw new InvalidNameException("Empty entry name is not allowed");

                if (subj.equals(""))
                    throw new InvalidSubjectException("Empty subject name is not allowed");

                entry = new Entry(name, subj, type, deadline);
            } catch (IllegalDateException exception) {
                JOptionPane.showMessageDialog(newEntry, "Illegal date time:\nNew entry is in the past", "Illegal Date Exception", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (InvalidNameException exception) {
                JOptionPane.showMessageDialog(newEntry, "Invalid entry name:\nNew entry must be named", "Invalid Name Exception", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (InvalidSubjectException exception) {
                JOptionPane.showMessageDialog(newEntry, "Invalid subject name:\nNew entry must have a subject name", "Invalid Subject Exception", JOptionPane.ERROR_MESSAGE);
                return;
            }

            entryManager.addEntry(entry);

            pnlEntries.add(Box.createRigidArea(new Dimension(0, 10)));
            pnlEntries.add(new EntryPanel(entry));

            System.out.println("new entry added");

            pnlEntries.removeAll();
            for (Entry todo : entryManager.getEntries()) {
                pnlEntries.add(Box.createRigidArea(new Dimension(0, 10)));
                pnlEntries.add(new EntryPanel(todo));
            }

            scpEntries.revalidate();
            scpEntries.repaint();
            pnlEntries.revalidate();
            pnlEntries.repaint();
        }
    }

    // contains all the entries
    private EntryManager entryManager;
    private JScrollPane scpEntries;
    private JPanel pnlEntries;

    private JButton btnNewEntry;

    EntryManagerPanel() {
        setLayout(new BorderLayout());

        entryManager = new EntryManager();

        pnlEntries = new JPanel();
        pnlEntries.setLayout(new BoxLayout(pnlEntries, BoxLayout.Y_AXIS));

        scpEntries = new JScrollPane(pnlEntries);
        scpEntries.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        btnNewEntry = new JButton("Add new entry");
        btnNewEntry.addActionListener(this);

        add(scpEntries, BorderLayout.CENTER);
        add(btnNewEntry, BorderLayout.SOUTH);

        // loads all saved entries into manager panel
        for (Entry todo : entryManager.getEntries()) {
            pnlEntries.add(Box.createRigidArea(new Dimension(0, 10)));
            pnlEntries.add(new EntryPanel(todo));
        }

        scpEntries.revalidate();
        scpEntries.repaint();
        pnlEntries.revalidate();
        pnlEntries.repaint();

        setPreferredSize(new Dimension(400, 600));
    }
}
