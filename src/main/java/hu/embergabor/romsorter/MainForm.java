package hu.embergabor.romsorter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    private JFileChooser textField1;
    private JFileChooser textField2;
    private JCheckBox createRegionSubFoldersCheckBox;
    private JCheckBox createAlphabeticalSubFoldersCheckBox;
    private JCheckBox enableRomTypeFilteringRomtypesCheckBox;
    private JCheckBox aAlternateCheckBox;
    private JCheckBox tTrainedCheckBox;
    private JCheckBox pPirateCheckBox;
    private JCheckBox bBadDumpCheckBox;
    private JCheckBox fFixedCheckBox;
    private JCheckBox tTranslationCheckBox;
    private JCheckBox hHackCheckBox;
    private JCheckBox oOverdumpCheckBox;
    private JCheckBox verifiedGoodDumpCheckBox;
    private JCheckBox unlUnlicensedCheckBox;
    private JButton startSortingButton;
    private JPanel fopanel;

    public MainForm() {
        startSortingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    List<String> filterList = new ArrayList<>();
                    if(enableRomTypeFilteringRomtypesCheckBox.isSelected()){
                        if(aAlternateCheckBox.isSelected()){
                            filterList.add("a");
                        }
                        if(tTrainedCheckBox.isSelected()){
                            filterList.add("t");
                        }
                        if(pPirateCheckBox.isSelected()){
                            filterList.add("p");
                        }
                        if(bBadDumpCheckBox.isSelected()){
                            filterList.add("b");
                        }
                        if(fFixedCheckBox.isSelected()){
                            filterList.add("f");
                        }
                        if(tTranslationCheckBox.isSelected()){
                            filterList.add("T");
                        }
                        if(hHackCheckBox.isSelected()){
                            filterList.add("h");
                        }
                        if(oOverdumpCheckBox.isSelected()){
                            filterList.add("o");
                        }
                        if(verifiedGoodDumpCheckBox.isSelected()){
                            filterList.add("!");
                        }
                        if(unlUnlicensedCheckBox.isSelected()){
                            filterList.add("U");
                        }
                    }
                    Integer movedfiles = Sorter.start(textField1.getSelectedFile().toString(), textField2.getSelectedFile().toString(), filterList, createAlphabeticalSubFoldersCheckBox.isSelected(), createRegionSubFoldersCheckBox.isSelected(), false);
                    JOptionPane.showMessageDialog(null, movedfiles + " ROMs copied!");
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().fopanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
