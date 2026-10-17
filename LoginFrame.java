import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public final class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    // Colors and input fields used by the login screen.
    private static final Color BACKGROUND = new Color(246, 248, 252);
    private static final Color NAVY = new Color(25, 52, 88);
    private static final Color BLUE = new Color(45, 118, 210);
    private static final Color BORDER = new Color(220, 230, 243);
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private JButton loginButton;

    /** Sets up the login window and makes Enter submit the login form. */
    public LoginFrame() {
        setTitle("JavaCup | Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 580));
        setSize(960, 640);
        setLocationRelativeTo(null);
        setContentPane(buildContent());
        getRootPane().setDefaultButton(loginButton);
    }

    /** Splits the login screen into a branding panel and a credential form. */
    private JPanel buildContent() {
        JPanel root = new JPanel(new java.awt.GridLayout(1, 2, 0, 0));
        root.add(buildBrandPanel());
        root.add(buildLoginPanel());
        return root;
    }

    /** Builds the left branding panel with the logo and store details. */
    private JPanel buildBrandPanel() {
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(42, 42, 38, 42));
        brandPanel.setBackground(NAVY);

        JLabel eyebrow = new JLabel("MILK TEA  ·  FRUIT TEA  ·  MADE FRESH");
        eyebrow.setAlignmentX(CENTER_ALIGNMENT);
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 11));
        eyebrow.setForeground(new Color(190, 211, 237));
        brandPanel.add(Box.createVerticalGlue());
        brandPanel.add(eyebrow);
        brandPanel.add(Box.createVerticalStrut(24));

        JPanel logoFrame = new JPanel(new BorderLayout());
        logoFrame.setBackground(Color.WHITE);
        logoFrame.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 110), 1),
                new EmptyBorder(12, 12, 12, 12)));
        logoFrame.setAlignmentX(CENTER_ALIGNMENT);
        logoFrame.setMaximumSize(new Dimension(330, 270));
        logoFrame.add(new JLabel(loadLogo(300, 225), SwingConstants.CENTER), BorderLayout.CENTER);
        brandPanel.add(logoFrame);
        brandPanel.add(Box.createVerticalStrut(24));

        JLabel headline = new JLabel("A fresh moment,");
        headline.setAlignmentX(CENTER_ALIGNMENT);
        headline.setFont(new Font("SansSerif", Font.BOLD, 29));
        headline.setForeground(Color.WHITE);
        JLabel secondLine = new JLabel("made for you.");
        secondLine.setAlignmentX(CENTER_ALIGNMENT);
        secondLine.setFont(new Font("SansSerif", Font.BOLD, 29));
        secondLine.setForeground(new Color(139, 190, 255));
        brandPanel.add(headline);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(secondLine);

        JLabel detail = new JLabel("Affordable, high-quality milk tea and fruit tea made fresh daily");
        detail.setAlignmentX(CENTER_ALIGNMENT);
        detail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        detail.setForeground(new Color(208, 222, 240));
        brandPanel.add(Box.createVerticalStrut(12));
        brandPanel.add(detail);
        brandPanel.add(Box.createVerticalGlue());
        return brandPanel;
    }

    /** Builds the right panel with the username, password, and login button. */
    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(48, 48, 42, 48));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(34, 32, 30, 32)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel badge = new JLabel("  SECURE STAFF ACCESS  ");
        badge.setAlignmentX(LEFT_ALIGNMENT);
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(BLUE);
        badge.setOpaque(true);
        badge.setBackground(new Color(235, 243, 255));
        badge.setBorder(new EmptyBorder(7, 8, 7, 8));
        card.add(badge);
        card.add(Box.createVerticalStrut(22));

        JLabel title = new JLabel("Welcome back");
        title.setAlignmentX(LEFT_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(NAVY);
        card.add(title);

        JLabel subtitle = new JLabel("Sign in to continue to your point of sale.");
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(105, 122, 145));
        card.add(Box.createVerticalStrut(7));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 7, 0);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        usernameLabel.setForeground(NAVY);
        fields.add(usernameLabel, constraints);

        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 16, 0);
        styleField(usernameField);
        fields.add(usernameField, constraints);

        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 7, 0);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        passwordLabel.setForeground(NAVY);
        fields.add(passwordLabel, constraints);

        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 22, 0);
        styleField(passwordField);
        fields.add(passwordField, constraints);
        card.add(fields);

        loginButton = createLoginButton();
        loginButton.setAlignmentX(LEFT_ALIGNMENT);
        loginButton.addActionListener(event -> login());
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        card.add(loginButton);

        JLabel hint = new JLabel("Need access? Askadministrator - Binos, Pineda, De Dios");
        hint.setAlignmentX(LEFT_ALIGNMENT);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(new Color(105, 122, 145));
        card.add(Box.createVerticalStrut(18));
        card.add(hint);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    /** Applies a consistent font, size, background, and border to an input field. */
    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(320, 46));
        field.setBackground(new Color(250, 251, 253));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 216, 231)),
                new EmptyBorder(9, 12, 9, 12)));
    }

    /** Creates the button used to submit the login credentials. */
    private JButton createLoginButton() {
        JButton button = new JButton("LOG IN");
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(NAVY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(15, 18, 15, 18));
        return button;
    }

    /**
     * Checks the entered credentials.
     * Opens the POS if they are correct; otherwise, shows an error and clears the password.
     */
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if ("Dream Team6.0".equals(username) && "JAVACUP".equals(password)) {
            new MilkTeaPOS().setVisible(true);
            dispose();
            return;
        }
        JOptionPane.showMessageDialog(this, "Incorrect username or password.",
                "Login failed", JOptionPane.ERROR_MESSAGE);
        passwordField.setText("");
        passwordField.requestFocusInWindow();
    }

    /** Loads and scales the logo image to fit the requested dimensions. */
    private ImageIcon loadLogo(int width, int height) {
        File file = new File("LOGO.jpg");
        if (!file.isFile()) {
            JOptionPane.showMessageDialog(this,
                    "The JavaCup logo file LOGO.jpg could not be found.",
                    "Missing logo", JOptionPane.WARNING_MESSAGE);
            return new ImageIcon();
        }
        ImageIcon source = new ImageIcon(file.getAbsolutePath());
        double scale = Math.min((double) width / source.getIconWidth(),
                (double) height / source.getIconHeight());
        int scaledWidth = Math.max(1, (int) Math.round(source.getIconWidth() * scale));
        int scaledHeight = Math.max(1, (int) Math.round(source.getIconHeight() * scale));
        Image scaled = source.getImage().getScaledInstance(
                scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
