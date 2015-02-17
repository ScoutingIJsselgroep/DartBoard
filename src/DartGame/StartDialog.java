package DartGame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class StartDialog extends JDialog {
    private JPanel contentPane;
    private JButton startOK;
    private JButton exitButton;
    private JList playerlist;
    private JTextField nameTextfield;
    private JButton addButton;
    private JButton deleteButton;
    private JSlider slider1;
    private JLabel sliderLabel;

    public StartDialog() {
        setContentPane(contentPane);
        playerlist.setModel(new DefaultListModel());

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addListItem();
            }
        });

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderLabel.setText(slider1.getValue() + "");
            }
        });
        startOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        nameTextfield.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    addListItem();
                }
            }
        });



        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });


    }

    private void addListItem() {
        ListModel model = playerlist.getModel();
        String[] playerArray = new String[model.getSize() + 1];
        for (int i = 0; i < model.getSize(); i++) {
            playerArray[i] = (String) model.getElementAt(i);
        }
        playerArray[model.getSize()] = nameTextfield.getText();
        playerlist.setListData(playerArray);
        revalidate();
        repaint();
        nameTextfield.setText("");
        nameTextfield.requestFocus();
    }

    private void onOK() {
        // add your code
        ListModel model = playerlist.getModel();
        String[] playerArray = new String[model.getSize()];
        for(int i = 0; i < model.getSize(); i++){
            playerArray[i] = (String) model.getElementAt(i);
        }

        BoardGUI gui = new BoardGUI(playerArray, slider1.getValue());
        dispose();
    }

    public static void main(String[] args) {
        StartDialog dialog = new StartDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
