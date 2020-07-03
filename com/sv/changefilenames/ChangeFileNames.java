package com.sv.changefilenames;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * User: Shailendra Verma
 * Date: Aug 28, 2006
 * Time: 3:30:57 PM
 * <p>
 * Change file names performs different operations
 * on file names by renaming them.
 * <p>
 * View usage for help.
 */
public class ChangeFileNames extends AppFrame {

    private final MyLogger logger;

    private JLabel lblFolder, lblAction, lblParam1, lblParam2, lblExt, lblLoading;
    private JTextField txtFolder, txtParam1, txtParam2, txtExt;
    //private JCheckBox jcSubFolder, jcProcessFolders, jcOverwrite, jcAppendFolder, jcUpdateID3v2Tag, jcModifyTitle;
    private JCheckBox jcSubFolder, jcProcessFolders, jcAppendFolder;
    private JComboBox<ChoiceInfo> jcb;
    private JTextArea taStatus;
    private JButton btnPreview, btnChange, btnBrowse, btnClear, btnUsage, btnExit;
    private JPanel jpSouth;
    private JPanel jpNorth;

    private static int totalProcessedFiles = 0;
    private static int totalChangedFiles = 0;
    private static int successfullyProcessedFiles = 0;
    private static int unprocessedFiles = 0;

    private final String title = "Change File Names";
    private final String EMPTY = Utils.EMPTY;

    private Arguments.OperationName operation;
    private DefaultConfigs configs;

    private ChangeFileNames() {
        logger = MyLogger.createLogger("ChangeFileNames.log");
        configs = new DefaultConfigs(logger);
        setTitle(title);
        initComponents();
    }

    private void initComponents() {

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitApp();
            }
        });

        lblFolder = new JLabel("Folder");
        lblAction = new JLabel("Action");
        lblLoading = new JLabel(new ImageIcon("loading.gif"));
        lblParam1 = new JLabel("Param 1");
        lblParam2 = new JLabel("Param 2");
        lblExt = new JLabel("Process files with ext.");

        //txtFolder = new JTextField("E:\\Songs\\2017", 20);
        txtFolder = new JTextField(configs.getConfig(DefaultConfigs.Config.LOCATION), 20);
        txtParam1 = new JTextField(EMPTY, 5);
        txtParam2 = new JTextField(EMPTY, 5);
        txtExt = new JTextField(configs.getConfig(DefaultConfigs.Config.EXTENSION), 5);

        ChoiceInfo[] allChoices = ChoiceInfo.values();
        Arrays.sort(allChoices, new ChoicesComparator());
        jcb = new JComboBox<>(allChoices); //drop down Options
        String actionConfig = configs.getConfig(DefaultConfigs.Config.ACTION);
        if (isValidAction (actionConfig)) {
            jcb.setSelectedItem(ChoiceInfo.valueOf(actionConfig));
        }
        //jcb.setSelectedItem(ChoiceInfo.APPEND_STRING_IN_START);

        CheckboxInfo checkbox = CheckboxInfo.SUB_FOLDER;
        jcSubFolder = new JCheckBox(checkbox.getLabel(), checkbox.isSelected());
        jcSubFolder.setToolTipText(checkbox.getToolTip());

        checkbox = CheckboxInfo.PROCESS_FOLDER;
        jcProcessFolders = new JCheckBox(checkbox.getLabel(), checkbox.isSelected());
        jcProcessFolders.setToolTipText(checkbox.getToolTip());

        checkbox = CheckboxInfo.APPEND_FOLDER;
        jcAppendFolder = new JCheckBox(checkbox.getLabel(), checkbox.isSelected());
        jcAppendFolder.setToolTipText(checkbox.getToolTip());

        taStatus = new JTextArea(EMPTY);
        taStatus.setLineWrap(true);
        taStatus.setAutoscrolls(true);

        btnPreview = new JButton("Preview");
        btnChange = new JButton("Change");
        btnBrowse = new JButton("Browse");
        btnClear = new JButton("Clear Status");
        btnUsage = new JButton("Usage");
        btnExit = new JButton("Exit");

        drawUI();
        addActions();

        printUsage();
        setSize(new Dimension(1200, 500));
        setExtendedState(MAXIMIZED_BOTH);
    }

    private boolean isValidAction(String config) {
        try {
            ChoiceInfo.valueOf(config);
            return true;
        } catch (IllegalArgumentException e) {
            // no action
        }
        return false;
    }

    private void addActions() {
        btnBrowse.addActionListener(event -> btnBrowseAction());
        btnPreview.addActionListener(event -> btnPreviewAction());
        btnChange.addActionListener(event -> btnChangeAction());
        btnExit.addActionListener(event -> exitApp());
        btnUsage.addActionListener(event -> printUsage());
        btnClear.addActionListener(event -> taStatus.setText(EMPTY));
    }

    private void btnBrowseAction() {
        JFileChooser jfc = new JFileChooser(txtFolder.getText(), FileSystemView.getFileSystemView());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showDialog(this, "Select");
        try {
            if (jfc.getSelectedFile() != null) {
                txtFolder.setText(jfc.getSelectedFile().getCanonicalPath());
            }
        } catch (Exception e1) {
            printExceptionDetails("Error: " + e1.getMessage());
            e1.printStackTrace();
        }
    }

    private void btnChangeAction() {
        taStatus.setText(EMPTY);
        handleControls(false);
        operation = Arguments.OperationName.CHANGE;
        startAsyncProcessing();
    }

    private void btnPreviewAction() {
        taStatus.setText(EMPTY);
        handleControls(false);
        operation = Arguments.OperationName.PREVIEW;
        startAsyncProcessing();
    }

    private void startAsyncProcessing() {
        SwingWorker<Void, Void> mySwingWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    startProcessing(prepareArguments());
                } catch (Exception e) {
                    printMsg(e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
        };
        mySwingWorker.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("state")) {
                if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                    handleControls(true);
                }
            }
        });
        mySwingWorker.execute();
    }

    private void exitApp() {
        configs.saveConfig(this);
        logger.log("Goodbye");
        System.exit(0);
    }

    private void drawUI() {
        getContentPane().setLayout(new BorderLayout());

        JPanel jpUp = new JPanel();
        JPanel jpDown = new JPanel();
        JPanel jpMid = new JPanel();
        jpSouth = new JPanel();
        jpNorth = new JPanel();
        jpNorth.setLayout(new GridLayout(3, 1));
        jpUp.add(lblFolder);
        jpUp.add(txtFolder);
        jpUp.add(btnBrowse);
        jpUp.add(lblExt);
        jpUp.add(txtExt);
        jpUp.add(lblAction);
        jpUp.add(jcb);
        jpDown.add(lblParam1);
        jpDown.add(txtParam1);
        txtParam1.setText(configs.getConfig(DefaultConfigs.Config.PARAM1));
        jpDown.add(lblParam2);
        jpDown.add(txtParam2);
        txtParam2.setText(configs.getConfig(DefaultConfigs.Config.PARAM2));
        jpDown.add(btnPreview);
        jpDown.add(btnChange);
        jpDown.add(btnExit);
        jpDown.add(lblLoading);
        lblLoading.setVisible(false);
        jpMid.add(jcSubFolder);
        jpMid.add(jcProcessFolders);
        //jpMid.add(jcOverwrite);
        jpMid.add(jcAppendFolder);
        /*jpMid.add(jcUpdateID3v2Tag);
        jpMid.add(jcModifyTitle);*/
        jpSouth.add(btnClear);
        jpSouth.add(btnUsage);

        jpNorth.add(jpUp);
        jpNorth.add(jpMid);
        jpNorth.add(jpDown);

        add(jpNorth, BorderLayout.NORTH);
        add(jpSouth, BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);
        add(new JScrollPane(taStatus), BorderLayout.CENTER);
    }

    /**
     * Operates on file names after accessing
     * and tries to ignore errors up to maximum
     * extends.
     *
     * @param args parameters for processing filenames
     */
    private void process(Arguments args) {
        final String log = "process: ";

        printMsg(log + "Starting process [" + args.getOperationName().name() + "]");

        if (args.getFileType().equals("ALL")) {
            args.setFileType(null);
        }

        java.util.List<Path> paths = getFilesList(args);
        int size = paths.size();
        printMsg(log + "Files obtained after applying filter are [" + size + "]");

        if (size != 0) {
            BaseProcessor processor = getProcessor(args);

            if (processor == null) {
                printMsg(log + "Unable to find processor for arguments [" + args + "]");
                return;
            }

            int cnt = 1;
            StringBuilder sbConvertedDetails = new StringBuilder();
            for (Path path : paths) {
                updateTitle(((cnt * 100) / size) + "%");
                File file = path.toFile();
                cnt++;

                //TODO: Behavior UNCHECKED if folder renamed what will happen when its child processed for renaming
                //TODO: for now commenting
                // printMsg("is directory [" + file.isDirectory() + "], is file [" + file.isFile() + "]");
                boolean process = file.isFile()
                        ||
                        (file.isDirectory() && args.isProcessFolders() && args.getChoice().equals(ChoiceInfo.CONVERT_TO_TITLE_CASE));

                if (process) {
                    args.setFile(file);

                    String returnVal = processor.execute(args);
                    printMsg(log + "modified file name obtained as [" + returnVal + "]");

                    if (Utils.hasValue(returnVal) && !returnVal.equals(args.getFileNameNoExtn())) {
                        String renameToPath = file.getParent() + "\\" + returnVal;
                        if (!file.isDirectory()) {
                            renameToPath += "." + args.getFileType();
                        }

                        if (args.isOperationChange()) {
                            boolean status = file.renameTo(new File(renameToPath));
                            sbConvertedDetails.append(log).append("The operation status for renaming [").append(args.getFileNameNoExtn()).append("] to [").append(returnVal).append("] ").append("for file.getParent() [").append(file.getParent()).append("] is [").append(status).append("]");
                            sbConvertedDetails.append(System.lineSeparator());
                            totalChangedFiles++;
                        } else if (args.isOperationPreview()) {
                            sbConvertedDetails.append(log).append("File [").append(args.getFileName()).append("] ==> [").append(returnVal).append(".").append(args.getFileType()).append("]");
                            sbConvertedDetails.append(System.lineSeparator());
                            totalChangedFiles++;
                        }
                    } else {
                        printMsg(log + "No conversion required for [" + args.getFileNameNoExtn() + "].");
                    }

                    totalProcessedFiles++;
                    successfullyProcessedFiles++;
                } else {
                    printMsg(log + "Skipping [" + args.getFileNameNoExtn() + "].");
                    totalProcessedFiles++;
                    unprocessedFiles++;
                }
            }
            printMsg("--------Details about actual matched files----------");
            printMsg(sbConvertedDetails.toString());
            updateTitle("Done");

        } else {
            printMsg("No file to process.");
        }

        printMsg("Processing COMPLETE. Processing summary.total processed files [" + totalProcessedFiles +
                "], total processed successfully files [" + successfullyProcessedFiles +
                "], total changed files [" + totalChangedFiles +
                "], total processed failed files [" + unprocessedFiles + "]");

        resetProcessedFileCounters();
    }

    private BaseProcessor getProcessor(Arguments args) {
        final String log = "getProcessor: ";
        try {
            return (BaseProcessor) Class.forName(this.getClass().getPackage() + "." + args.getChoice().getValue())
                    .getConstructor(logger.getClass())
                    .newInstance(logger);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            printExceptionDetails(log + "Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method will return path based on option selected either of below:
     * - Filtered files path from root folder only or
     * - Filtered files path from root folder and sub-folder recursively or
     * - Filtered files path from root folder, sub-folder and sub-directories recursively
     *
     * @param args obj
     * @return list of path
     */
    private List<Path> getFilesList(Arguments args) {
        Stream<Path> paths = null;
        java.util.List<Path> listPaths = new ArrayList<>();
        try {
            if (args.isProcessSubFolder()) {
                SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<>() {

                    /**
                     * At present fetching only those sub-folders that has at least one matching file
                     * To fetch all folders irrespective of matching file another method need to be overridden
                     */
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (file.toString().endsWith(args.getFileType())) {
                            listPaths.add(file);

                            boolean shouldAddFolder = args.isProcessFolders()
                                    &&
                                    !Utils.createPath(file.toFile().getParent()).toString().equals(Utils.createPath(args.getSourceDir()).toString())
                                    &&
                                    !listPaths.contains(Utils.createPath(file.toFile().getParent()));
                            if (shouldAddFolder) {
                                listPaths.add(Utils.createPath(file.toFile().getParent()));
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                };
                Files.walkFileTree(Utils.createPath(args.getSourceDir()), visitor);
            } else {
                paths = Files.list(Utils.createPath(args.getSourceDir())).filter(path -> path.toString().endsWith(args.getFileType()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (paths != null) {
            paths.forEach(listPaths::add);
        }
        return listPaths;
    }

    private void resetProcessedFileCounters() {
        totalProcessedFiles = 0;
        totalChangedFiles = 0;
        successfullyProcessedFiles = 0;
        unprocessedFiles = 0;
    }

    /**
     * Prints message on console and/or text area
     *
     * @param s message to print
     */
    private void printMsg(String s) {
        taStatus.append(s + System.lineSeparator());
        s = "[" + new Date() + "]" + s;
        logger.log(s);
    }

    /**
     * Validate all the arguments.
     *
     * @param args arguments
     * @return boolean as status
     */
    private boolean validateAllArgs(Arguments args) {
        return (validateArgs(args) && validateDir(args.getSourceDir()) && args.getChoice() != null);
    }

    /**
     * Validate the arguments against not null condition.
     *
     * @param args parameters for processing filenames
     * @return boolean status of operation
     */
    private boolean validateArgs(Arguments args) {
        return (
                Utils.hasValue(args.getSourceDir()) ||
                        args.getChoice() != null ||
                        Utils.hasValue(args.getFileType()));
    }

    /**
     * checks if passed dir name exists and accessible
     *
     * @param dir directory name
     * @return boolean status of operation
     */
    private boolean validateDir(String dir) {
        File srcDir = new File(dir);
        boolean result = srcDir.exists() && srcDir.isDirectory();
        if (!result) {
            printExceptionDetails("Either path for directory [" + dir + "] does not exists or it is not a directory.");
        }
        return result;
    }

    /**
     * print the usage of class
     */
    private void printUsage() {
        String usage = "javac ChangeFileNames <srcDir> <include-sub-folders> <file-filter-extension> <option> [<extra-param>]"
                + "\nwhere"
                + "\n <srcDir> - source directory whose filenames are to be processed."
                + "\n <include-sub-folders> - TRUE to include and FALSE to exclude."
                + "\n <file-filter-extension> - the specific files (like .html or .class etc) to be processed. \"ALL\" for all files."
                + "\n <option> - the operation to be performed on files, also <extra-param> is optional and required by some of the operations of <option>. The valid options are:"
                + "\n     1.  REMOVE_NUMBERS_FROM_FILE_NAMES"
                + "\n     2.  REMOVE_NUMBERS_FROM_START"
                + "\n     3.  REMOVE_NUMBERS_FROM_END"
                + "\n     4.  APPEND_STRING_IN_START <string>"
                + "\n     5.  APPEND_STRING_IN_END <string>"
                + "\n     6.  REMOVE_CHARS_FROM_START <number-of-chars>"
                + "\n     7.  REMOVE_CHARS_FROM_END <number-of-chars>"
                + "\n     8.  REMOVE_SPACES_FROM_START"
                + "\n     9.  REMOVE_SPACES_FROM_END"
                + "\n     10. REMOVE_SPACES_FROM_BOTH_SIDES"
                + "\n     11. REMOVE_MATCH_FROM_START <string>"
                + "\n     12. REMOVE_MATCH_FROM_END <string>"
                + "\n     13. REMOVE_MATCH <string>"
                + "\n     14. REPLACE_MATCH_FROM_START <search-string> <replacement-string>"
                + "\n     15. REPLACE_MATCH_FROM_END <search-string> <replacement-string>"
                + "\n     16. REPLACE_MATCH <search-string> <replacement-string>"
                + "\n     17. CONVERT_TO_TITLE_CASE"
                + "\n     18. UPDATE_MP3_TAGS (obsolete)"
                + "\n\n example 1. javac ChangeFileNames c:/test FALSE mp3 REMOVE_NUMBERS_FROM_START"
                + "\n example 2. javac ChangeFileNames c:/test FALSE ALL REMOVE_NUMBERS_FROM_FILE_NAMES"
                + "\n example 3. javac ChangeFileNames c:/test FALSE java APPEND_STRING_IN_START prefix"
                + "\n example 4. javac ChangeFileNames c:/test FALSE class REMOVE_CHARS_FROM_END 4"
                + "\n example 5. javac ChangeFileNames c:/test FALSE class REMOVE_SPACES_FROM_END"
                + "\n example 6. javac ChangeFileNames c:/test FALSE class REPLACE_MATCH abc xyz";

        taStatus.setText("Usage = " + usage);
    }

    /**
     * print the usage and then throws the exception with the
     * given message.
     *
     * @param msg string for exception message
     */
    private void printExceptionDetails(String msg) {
        printMsg(msg);
    }

    /**
     * entry unit point
     *
     * @param args parameters for processing filenames
     */
    public static void main(String[] args) {
        new ChangeFileNames();
    }

    private Arguments prepareArguments() {
        Arguments arguments = new Arguments();

        arguments.setSourceDir(txtFolder.getText());
        arguments.setFileType(txtExt.getText());
        arguments.setChoice(((ChoiceInfo) (jcb.getSelectedItem())));
        arguments.setParam1(Utils.hasValue(txtParam1.getText()) ? txtParam1.getText() : EMPTY);
        arguments.setParam2(Utils.hasValue(txtParam2.getText()) ? txtParam2.getText() : EMPTY);

        arguments.setAppendFolder(jcAppendFolder.isSelected());
        arguments.setProcessFolders(jcProcessFolders.isSelected());
        arguments.setProcessSubFolder(jcSubFolder.isSelected());
        arguments.setOperationName(operation);

        printMsg("prepareArguments: " + arguments.toString());
        return arguments;
    }

    /**
     * This method provides support to execute it from command line.
     * As of thought removing this support from command line on 16-Oct-2017
     *
     * @param args of type {@link Arguments}
     */
    private void startProcessing(Arguments args) {
        if (args != null && validateAllArgs(args)) {
            process(args);
        } else {
            printMsg("Unable to start process.");
        }
    }

    private void handleControls(boolean enable) {
        showLoading(enable);
        jpNorth.setEnabled(enable);
        jpSouth.setEnabled(enable);
    }

    private void showLoading(boolean enable) {
        lblLoading.setVisible(!enable);
    }

    private void updateTitle(String addlInfo) {
        setTitle(Utils.hasValue(addlInfo) ? title + Utils.SP_DASH_SP + addlInfo : title);
    }

    public String getLocationVal() {
        return txtFolder.getText();
    }

    public String getTxtParam1() {
        return txtParam1.getText();
    }

    public String getTxtParam2() {
        return txtParam2.getText();
    }

    public String getTxtExt() {
        return txtExt.getText();
    }

    public String getAction() {
        return ((ChoiceInfo)(jcb.getSelectedItem())).name();
    }
}
