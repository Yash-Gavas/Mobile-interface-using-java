import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class MobileInterfaceApp extends JFrame {

    private JTextField passwordField;
    private boolean unlockClicked = false;
    private JPanel passwordPanel;
    private JPanel unlockMessagePanel;
    private JPanel clockOptionsPanel; // New panel for clock options
    private JPanel galleryPanel; // New panel for the gallery

    private Timer timer;
    private int secondsRemaining;
    private JLabel timerLabel;
    private JLabel stopwatchLabel; // New label for stopwatch time
    private JLabel liveTimeLabel;
    private JLabel alarmLabel; // Label to display alarm set for
    private boolean isAlarmSet = false; // Flag to check if alarm is set

    private JLabel selectedImageLabel;
    private JButton deleteButton;


    public MobileInterfaceApp() {
        setTitle("Mobile Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        unlockMessagePanel = new JPanel();
        JLabel unlockMessageLabel = new JLabel("Click to Unlock");
        unlockMessagePanel.add(unlockMessageLabel);

        passwordPanel = new JPanel();
        passwordField = new JPasswordField(15);
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyPassword();
            }
        });
        passwordPanel.add(new JLabel("Enter Password"));
        passwordPanel.add(passwordField);
        passwordPanel.add(enterButton);

        galleryPanel = new JPanel(); // Initialize the gallery panel
        JLabel galleryLabel = new JLabel("Gallery Page");
        galleryPanel.add(galleryLabel);

        setWallpaper("photosbackround.jpg", galleryPanel); // Change the wallpaper image accordingly

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!unlockClicked) {
                    unlockMessageLabel.setText("");
                    setContentPane(new JLabel());
                    setWallpaper("wallpaper.jpg", passwordPanel);
                    unlockClicked = true;
                    validate();
                    repaint();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setWallpaper("wallpaper.jpg", unlockClicked ? passwordPanel : unlockMessagePanel);
            }
        });
    }

    private void setWallpaper(String imagePath, JPanel overlayPanel) {
        try {
            File wallpaperFile = new File(imagePath);

            if (wallpaperFile.exists()) {
                Image wallpaperImage = ImageIO.read(wallpaperFile);

                int width = getWidth();
                int height = getHeight();

                if (width > 0 && height > 0) {
                    Image scaledImage = wallpaperImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    setContentPane(new JLabel(new ImageIcon(scaledImage)));

                    if (overlayPanel != null) {
                        // Calculate center position for Overlay Panel
                        int overlayX = (width - overlayPanel.getPreferredSize().width) / 2;
                        int overlayY = (height - overlayPanel.getPreferredSize().height) / 2;

                        JPanel overlayPositionPanel = new JPanel(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        overlayPositionPanel.add(overlayPanel, gbc);

                        // Set position
                        setLayout(null);
                        overlayPositionPanel.setBounds(overlayX, overlayY, overlayPanel.getPreferredSize().width, overlayPanel.getPreferredSize().height);

                        add(overlayPositionPanel);
                    }

                    validate();
                } else {
                    System.err.println("Invalid width or height. Width: " + width + ", Height: " + height);
                }
            } else {
                System.err.println("Wallpaper not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyPassword() {
        String enteredPassword = passwordField.getText();
        if ("7979".equals(enteredPassword)) {
            showHomePage(); // This method is called when the password is correct
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect password. Try again.");
        }
    }

    private void showHomePage() {
        // Reset the content pane
        setContentPane(new JLabel());

        // Set wallpaper for home page without welcome message and add wallpaper option
        setWallpaper("wallpaper1.jpg", null); // Pass null for overlayPanel

        // Create components for the home page
        JButton icon1 = createIconButton("calender.jpg");
        JButton icon2 = createIconButton("clock.jpg");
        JButton icon3 = createIconButton("contacts.jpg");
        JButton icon4 = createIconButton("message.jpg");
        JButton icon5 = createIconButton("photos.jpg");
        JButton icon6 = createIconButton("setting.jpg");

        // Set positions for each icon in two rows with 5cm spacing
        setIconPosition(icon1, 300, 150);
        setIconPosition(icon2, 800, 150);
        setIconPosition(icon3, 1300, 150);
        setIconPosition(icon4, 300, 500);
        setIconPosition(icon5, 800, 500);
        setIconPosition(icon6, 1300, 500);

        // Add action listeners if needed

        // Add each icon to the content pane
        add(icon1);
        add(icon2);
        add(icon3);
        add(icon4);
        add(icon5);
        add(icon6);

        validate();
        repaint();
    }

    private JButton createIconButton(String iconPath) {
        // Create button with icon and name
        JButton iconButton = new JButton();
        try {
            Image iconImage = ImageIO.read(new File(iconPath));

            // Set icon
            ImageIcon icon = new ImageIcon(iconImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
            iconButton.setIcon(icon);

            // Set icon name below the icon with increased font size
            iconButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            iconButton.setHorizontalTextPosition(SwingConstants.CENTER);
            String iconName = new File(iconPath).getName().replaceAll(".jpg", "");
            iconButton.setText("<html><center><font size='5'>" + iconName + "</font></center></html>");

            // Remove border and background, and ensure the icon image takes up the entire size of the button
            iconButton.setBorderPainted(false);
            iconButton.setFocusPainted(false);
            iconButton.setContentAreaFilled(false);

            // Set foreground color to ensure text visibility
            iconButton.setForeground(Color.BLACK);

            // Add action listener to the clock icon
            if ("clock".equalsIgnoreCase(iconName)) {
                iconButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showClockOptions();
                    }
                });
            }

            // Add action listener to the photos icon
            if ("photos".equalsIgnoreCase(iconName)) {
                iconButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showGalleryPage();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return iconButton;
    }

    private void setIconPosition(JButton iconButton, int x, int y) {
        // Set position for the icon with 5cm spacing
        iconButton.setBounds(x, y, 250, 250); // Original size is doubled
    }

    private void showClockOptions() {
        // Reset the content pane
        setContentPane(new JLabel());

        // Set wallpaper for clock options page
        setWallpaper("Background 3.jpg", null); // Change the wallpaper image accordingly

        // Create components for the clock options page
        JButton timerButton = new JButton("Start Timer");
        JButton alarmButton = new JButton("Set Alarm");
        JButton stopwatchButton = new JButton("Stopwatch");
        JButton backButton = createBackButton(); // Use the method to create a back button

        timerLabel = new JLabel("Timer: ");
        stopwatchLabel = new JLabel("Stopwatch: "); // New label for stopwatch time
        liveTimeLabel = new JLabel("Time: " + getLiveTime()); // Display live time without a button
        alarmLabel = new JLabel("Alarm set for: "); // Label to display alarm set for

        // Add action listeners to the buttons
        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle between starting and stopping the timer
                if (timer == null) {
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });

        alarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAlarmOptions();
            }
        });

        stopwatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle between starting and stopping the stopwatch
                if (timer == null) {
                    startStopwatch();
                } else {
                    stopTimer(); // Stop the timer if it's running
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to the home page
                showHomePage();
            }
        });

        // Set button foreground (text color) and background color
        timerButton.setForeground(Color.WHITE);
        timerButton.setBackground(Color.BLACK);

        alarmButton.setForeground(Color.WHITE);
        alarmButton.setBackground(Color.BLACK);

        stopwatchButton.setForeground(Color.WHITE);
        stopwatchButton.setBackground(Color.BLACK);

        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);

        // Set positions for each button
        setClockOptionButtonPosition(timerButton, 1300, 250);
        setClockOptionButtonPosition(alarmButton, 1300, 450);
        setClockOptionButtonPosition(stopwatchButton, 1300, 650);

        setClockOptionLabelPosition(timerLabel, 1300, 350);
        setClockOptionLabelPosition(stopwatchLabel, 1300, 750); // Position adjusted for stopwatch
        setClockOptionLabelPosition(liveTimeLabel, 800, 500); // Center of the clock icon
        setClockOptionLabelPosition(alarmLabel, 1300, 550); // Position for alarm label

        setClockOptionButtonPosition(backButton, 50, 50); // Adjust the position accordingly

        // Add each component to the content pane
        add(timerButton);
        add(alarmButton);
        add(stopwatchButton);
        add(timerLabel);
        add(stopwatchLabel);
        add(liveTimeLabel);
        add(alarmLabel);
        add(backButton);

        // Start a timer to update live time every second
        Timer liveTimeUpdater = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                liveTimeLabel.setText("Time: " + getLiveTime());
            }
        });
        liveTimeUpdater.start();

        validate();
        repaint();
    }
    private void addGalleryMouseListener() {
        galleryPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Clear selection when clicking on the gallery panel
                selectedImageLabel = null;
                deleteButton = null;
            }
        });
    }
    

    private void showGalleryPage() {
        // Reset the content pane
        setContentPane(new JLabel());
    
        // Set wallpaper for the gallery page
        setWallpaper("photosbackround.jpg", null); // Change the wallpaper image accordingly
    
        // Add the following line to make sure the mouse listener is added to the galleryPanel
        addGalleryMouseListener();
        // Create components for the gallery page
        JButton backButton = createBackButton();
        JButton addPhotosButton = createAddPhotosButton(); // New button to add photos

        // Set button foreground (text color) and background color
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);

        addPhotosButton.setForeground(Color.WHITE);
        addPhotosButton.setBackground(Color.BLACK);

        // Set position for the back button
        backButton.setBounds(50, 50, 100, 30);

        // Set position for the add photos button
        addPhotosButton.setBounds(1300, 50, 150, 30);

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHomePage();
            }
        });

        // Add action listener to the add photos button
        addPhotosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int result = fileChooser.showOpenDialog(MobileInterfaceApp.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    for (File file : selectedFiles) {
                        // Handle the selected files (e.g., copy or move them to a specific directory)
                        // You can also display the selected images on the gallery page
                        displayImageInGallery(file);
                    }
                }
            }
        });

        // Add each component to the content pane
        add(backButton);
        add(addPhotosButton);

        validate();
        repaint();
    }

    private JButton createAddPhotosButton() {
        JButton addPhotosButton = new JButton("Add Photos");
        addPhotosButton.setForeground(Color.WHITE);
        addPhotosButton.setBackground(Color.BLACK);

        addPhotosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int result = fileChooser.showOpenDialog(MobileInterfaceApp.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    for (File file : selectedFiles) {
                        // Handle the selected files (e.g., copy or move them to a specific directory)
                        // You can also display the selected images on the gallery page
                        displayImageInGallery(file);
                    }
                }
            }
        });

        return addPhotosButton;
    }

    private void displayImageInGallery(File imageFile) {
        try {
            Image image = ImageIO.read(imageFile);
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(150, 150, Image.SCALE_SMOOTH));

            JLabel imageLabel = new JLabel(imageIcon);

            // Add mouse listener to handle image clicks
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showSelectedImage(imageIcon);
                }
            });

            galleryPanel.add(imageLabel);

            validate();
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSelectedImage(ImageIcon imageIcon) {
        // Create a new frame to display the selected image in detail view
        JFrame detailFrame = new JFrame("Selected Image");
        detailFrame.setSize(500, 500);
        detailFrame.setLocationRelativeTo(null);

        // Create label to display the selected image
        selectedImageLabel = new JLabel(imageIcon);
        detailFrame.add(selectedImageLabel);

        // Create delete button to delete the selected image
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedImage();
                detailFrame.dispose(); // Close the detail view frame after deletion
            }
        });

        // Add delete button to the frame
        detailFrame.add(deleteButton, BorderLayout.SOUTH);

        detailFrame.setVisible(true);
    }

    private void deleteSelectedImage() {
        // Remove the selected image from the gallery panel
        if (selectedImageLabel != null) {
            galleryPanel.remove(selectedImageLabel);
            validate();
            repaint();
        }
    }
    private void showAlarmOptions() {
        String inputTime = JOptionPane.showInputDialog(this, "Enter alarm time in HH:mm format:");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            Date alarmDate = sdf.parse(inputTime);

            scheduleAlarm(alarmDate);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid time in HH:mm format.");
        }
    }

    private void scheduleAlarm(Date alarmDate) {
        javax.swing.Timer alarmTimer = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MobileInterfaceApp.this, "Alarm! Wake up!");
                alarmLabel.setText("<html><font size='5'>Alarm set for: " + new SimpleDateFormat("HH:mm").format(alarmDate) + "</font></html>");
                isAlarmSet = true; // Set the flag when the alarm is set
            }
        });

        long delay = alarmDate.getTime() - System.currentTimeMillis();
        if (delay > 0) {
            alarmTimer.setInitialDelay((int) delay);
            alarmTimer.setRepeats(false); // Run the timer only once
            alarmTimer.start();
        }

        if (!isAlarmSet) { // Check if the alarm is not set yet
            alarmLabel.setText("<html><font size='5'>Alarm set for: " + new SimpleDateFormat("HH:mm").format(alarmDate) + "</font></html>");
            isAlarmSet = true; // Set the flag when the alarm is set
        }
    }

    private void setClockOptionLabelPosition(JLabel label, int x, int y) {
        int labelWidth = 500; // Increase the width to accommodate the larger text
        int labelHeight = 200; // Adjust the height accordingly

        label.setBounds(x - labelWidth / 2, y - labelHeight / 2, labelWidth, labelHeight);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, label == timerLabel || label == stopwatchLabel ? 20 : 60)); // Adjust the font size
        label.setForeground(Color.BLACK); // Change the text color if needed
    }

    private void setClockOptionButtonPosition(JButton button, int x, int y) {
        // Set position for the clock option button with 5cm spacing
        button.setBounds(x, y, 200, 50); // Adjust the size accordingly
    }

    private void startTimer() {
        String inputTime = JOptionPane.showInputDialog(this, "Enter timer duration in minutes:");
        try {
            int timerDuration = Integer.parseInt(inputTime);
            secondsRemaining = timerDuration * 60;

            if (timer == null) {
                // Start the timer
                timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (secondsRemaining > 0) {
                            timerLabel.setText("Timer: " + formatTime(secondsRemaining));
                            secondsRemaining--;
                        } else {
                            stopTimer();
                            JOptionPane.showMessageDialog(MobileInterfaceApp.this, "Timer complete!");
                        }
                    }
                });
                timer.start();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number for timer duration.");
        }
    }

    private void startStopwatch() {
        if (timer == null) {
            // Start the stopwatch
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    secondsRemaining++;
                    stopwatchLabel.setText("Stopwatch: " + formatTime(secondsRemaining));
                }
            });
            timer.start();
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
            timerLabel.setText("Timer: ");
            stopwatchLabel.setText("Stopwatch: "); // Reset stopwatch label
        }
    }

    // Helper method to format remaining time
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private String getLiveTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    private JButton createBackButton() {
        // Create back button with arrow icon
        JButton backButton = new JButton("Back");
        try {
            Image arrowImage = ImageIO.read(new File("arrow.jpg")); // Replace with the actual arrow icon image

            // Set arrow icon
            ImageIcon arrowIcon = new ImageIcon(arrowImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            backButton.setIcon(arrowIcon);

            // Remove border and background
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setContentAreaFilled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return backButton;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MobileInterfaceApp().setVisible(true);
            }
        });
    }
}
