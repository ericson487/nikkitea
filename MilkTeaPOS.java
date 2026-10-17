import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicToggleButtonUI;

public final class MilkTeaPOS extends JFrame {
    private static final long serialVersionUID = 1L;

    // Colors and default values shared across the POS screen.
    private static final Color BACKGROUND = new Color(243, 247, 252);
    private static final Color NAVY = new Color(25, 52, 88);
    private static final Color BLUE = new Color(45, 118, 210);
    private static final Color PALE_BLUE = new Color(230, 239, 250);
    private static final Color BORDER = new Color(220, 230, 243);
    private static final Color MUTED = new Color(105, 122, 145);
    private static final Color ALERT_RED = new Color(190, 40, 50);
    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final int DEFAULT_INITIAL_STOCK = 20;
    private static final NumberFormat CURRENCY =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PH"));

    // Data collections for the menu, inventory, cart, and completed orders.
    private final ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    private final ArrayList<AddOn> availableAddOns = new ArrayList<AddOn>();
    private final Map<String, Integer> stockByItemId = new HashMap<String, Integer>();
    private final LinkedList<CartItem> cartItems = new LinkedList<CartItem>();
    private final LinkedList<Sale> completedOrders = new LinkedList<Sale>();
    private final JPanel menuGrid = new JPanel(new GridLayout(0, 3, 14, 14));
    private final JPanel filterButtons = new JPanel();
    private final ArrayList<JButton> categoryButtons = new ArrayList<JButton>();
    private final JPanel cartContents = new JPanel();
    private final JPanel pageContainer = new JPanel(new CardLayout());
    private final JPanel dashboardPanel = new JPanel(new BorderLayout(0, 18));
    private final JPanel inventoryPanel = new JPanel(new BorderLayout(0, 14));
    private final JLabel totalLabel = new JLabel();

    // UI components that need updates while the app is running.
    private JButton historyButton;
    private JButton dashboardButton;
    private JButton pointOfSaleButton;
    private JButton inventoryButton;
    private JToggleButton dineInButton;
    private JToggleButton takeOutButton;
    private String selectedCategory = "All";
    private String selectedOrderType = "Take out";
    private int nextReceiptNumber = 1;

    /**
     * Sets up the window, menu data, and main POS panels.
     * Builds the screen and opens the dashboard as the first page.
     */
    public MilkTeaPOS() {
        setTitle("JavaCup | Point of Sale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 680));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BACKGROUND);
        loadMenu();
        buildInterface();
        refreshMenu();
        refreshCart();
    }

    /**
     * Adds products to the menu and assigns their starting stock.
     * Each item starts with DEFAULT_INITIAL_STOCK units.
     */
    private void loadMenu() {
        menuItems.add(new MilkTea("MT01", "Hokkaido", 89, "HOKAIDO.jpg"));
        menuItems.add(new MilkTea("MT02", "Okinawa", 89, "OKINAWA.jpg"));
        menuItems.add(new MilkTea("MT03", "Wintermelon", 85, "WintermelonC.jpg"));
        menuItems.add(new MilkTea("MT04", "Taro", 85, "TARO.jpg"));
        menuItems.add(new MilkTea("MT05", "Matcha", 95, "Matcha.jpg"));
        menuItems.add(new MilkTea("MT06", "Dark Chocolate", 95, "Darkchoco.jpg"));
        menuItems.add(new MilkTea("MT07", "Cookies & Cream", 95, "CNC.jpg"));
        menuItems.add(new MilkTea("MT08", "Cheesecake", 99, "Cheesecake.jpg"));
        menuItems.add(new MilkTea("MT09", "Cream Cheese", 99, "Creamcheese.jpg"));

        menuItems.add(new FruitTea("FT01", "Mango", 85, "MANGO.jpg"));
        menuItems.add(new FruitTea("FT02", "Strawberry", 89, "Strawberry.jpg"));
        menuItems.add(new FruitTea("FT03", "Lychee", 85, "LYCHEE.jpg"));
        menuItems.add(new FruitTea("FT04", "Peach", 85, "peach.jpg"));
        menuItems.add(new FruitTea("FT05", "Green Apple", 85, "GreenAplle.jpg"));

        menuItems.add(new Snack("SN01", "Cake Slice", 80, "cakeslic.jpg"));
        menuItems.add(new Snack("SN02", "Pancake", 75, "pancake.jpg"));
        menuItems.add(new Snack("SN03", "Churros", 70, "churros.jpg"));
        menuItems.add(new Snack("SN04", "Waffles", 100, "waffles.jpg"));
        menuItems.add(new Snack("SN05", "Nachos", 90, "NCHOS.jpg"));
        menuItems.add(new Snack("SN06", "Mojos", 85, "MOJOS.jpg"));
        menuItems.add(new Snack("SN07", "Fries", 50, "fries.jpg"));
        menuItems.add(new Snack("SN08", "Popper Fries", 75, "popersfries.jpg"));

        AddOn pearls = new AddOn("AO01", "Pearl", 15, "Pearl.jpg");
        AddOn nata = new AddOn("AO02", "Nata de Coco", 15, "Nata.jpg");
        AddOn creamCheese = new AddOn("AO03", "Cream Cheese", 15, "Creamcheese.jpg");
        availableAddOns.add(pearls);
        availableAddOns.add(nata);
        availableAddOns.add(creamCheese);
        menuItems.add(pearls);
        menuItems.add(nata);
        menuItems.add(creamCheese);
        for (MenuItem item : menuItems) {
            stockByItemId.put(item.getId(), DEFAULT_INITIAL_STOCK);
        }
    }

    /**
     * Builds the main app layout: header at the top,
     * sidebar on the left, and page content in the center.
     */
    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(18, 20, 18, 20));
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setOpaque(false);
        content.add(buildSidebar(), BorderLayout.WEST);

        pageContainer.setOpaque(false);
        pageContainer.add(dashboardPanel, "dashboard");
        pageContainer.add(buildPointOfSalePanel(), "pos");
        pageContainer.add(inventoryPanel, "inventory");
        content.add(pageContainer, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);
        refreshDashboard();
        showPage("dashboard");
    }

    /**
     * Builds sidebar navigation for the dashboard, POS, inventory,
     * and menu category filters.
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 14, 18, 14)));
        sidebar.setPreferredSize(new Dimension(205, 0));

        JLabel brand = new JLabel("JavaCup");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(NAVY);
        brand.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(22));
        addSidebarSectionLabel(sidebar, "WORKSPACE");

        dashboardButton = createSidebarButton("Dashboard");
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showPage("dashboard");
            }
        });
        sidebar.add(dashboardButton);

        pointOfSaleButton = createSidebarButton("Point of Sale");
        pointOfSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showPage("pos");
            }
        });
        sidebar.add(pointOfSaleButton);
        inventoryButton = createSidebarButton("Inventory");
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showPage("inventory");
            }
        });
        sidebar.add(inventoryButton);
        sidebar.add(Box.createVerticalStrut(20));
        addSidebarSectionLabel(sidebar, "MENU CATEGORIES");

        filterButtons.setOpaque(false);
        filterButtons.setLayout(new BoxLayout(filterButtons, BoxLayout.Y_AXIS));
        addFilterButton(filterButtons, "All");
        addFilterButton(filterButtons, "Milk Tea");
        addFilterButton(filterButtons, "Fruit Tea");
        addFilterButton(filterButtons, "Snacks & Bites");
        sidebar.add(filterButtons);
        sidebar.add(Box.createVerticalGlue());

        JLabel note = new JLabel("<html>Sales and orders are kept<br>for this session.</html>");
        note.setFont(new Font("SansSerif", Font.PLAIN, 10));
        note.setForeground(MUTED);
        note.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(note);
        return sidebar;
    }

    /** Adds a small heading to separate sidebar sections. */
    private void addSidebarSectionLabel(JPanel sidebar, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(MUTED);
        label.setBorder(new EmptyBorder(0, 8, 8, 0));
        label.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(label);
    }

    /** Creates a consistently styled button for the sidebar. */
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMargin(new Insets(10, 12, 10, 8));
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setPreferredSize(new Dimension(175, 42));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        return button;
    }

    /**
     * Combines the menu and current order on the POS page:
     * the menu is in the center and the cart is on the right.
     */
    private JPanel buildPointOfSalePanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setOpaque(false);
        panel.add(buildMenuPanel(), BorderLayout.CENTER);
        JPanel orderPanel = buildOrderPanel();
        orderPanel.setPreferredSize(new Dimension(350, 0));
        panel.add(orderPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * Shows the selected page and updates the navigation style.
     * Refreshes the dashboard or inventory when either page is opened.
     */
    private void showPage(String page) {
        ((CardLayout) pageContainer.getLayout()).show(pageContainer, page);
        styleSidebarButton(dashboardButton, "dashboard".equals(page));
        styleSidebarButton(pointOfSaleButton, "pos".equals(page));
        styleSidebarButton(inventoryButton, "inventory".equals(page));
        updateInventoryAlert();
        if ("dashboard".equals(page)) {
            refreshDashboard();
        } else if ("inventory".equals(page)) {
            refreshInventory();
        }
    }

    /** Sets the sidebar button colors based on whether its page is selected. */
    private void styleSidebarButton(JButton button, boolean selected) {
        styleButton(button, selected ? PALE_BLUE : Color.WHITE,
                selected ? BLUE : NAVY);
    }

    /**
     * Shows a low-stock count on the Inventory button when needed.
     */
    private void updateInventoryAlert() {
        if (inventoryButton == null) {
            return;
        }
        int lowStockCount = getLowStockCount();
        if (lowStockCount > 0) {
            inventoryButton.setText("<html>Inventory <span style='color:#BE2832'>"
                    + "&#9679; " + lowStockCount + " LOW</span></html>");
            inventoryButton.setToolTipText(lowStockCount
                    + " item(s) at or below " + LOW_STOCK_THRESHOLD + " in stock");
        } else {
            inventoryButton.setText("Inventory");
            inventoryButton.setToolTipText("View and update item stock");
        }
    }

    /** Counts menu items whose stock is at or below the low-stock threshold. */
    private int getLowStockCount() {
        int count = 0;
        for (MenuItem item : menuItems) {
            if (getStock(item) <= LOW_STOCK_THRESHOLD) {
                count++;
            }
        }
        return count;
    }

    /** Gets an item's current stock using its unique ID. */
    private int getStock(MenuItem item) {
        Integer stock = stockByItemId.get(item.getId());
        return stock == null ? 0 : stock.intValue();
    }

    /**
     * Rebuilds the inventory list using the current menu and stock data.
     */
    private void refreshInventory() {
        inventoryPanel.removeAll();
        inventoryPanel.setOpaque(false);
        inventoryPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Inventory");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Track menu and add-on stock. Low stock is "
                + LOW_STOCK_THRESHOLD + " or fewer.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);
        inventoryPanel.add(heading, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setBackground(Color.WHITE);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBorder(new EmptyBorder(8, 16, 8, 16));
        for (final MenuItem item : menuItems) {
            rows.add(createInventoryRow(item));
        }
        JScrollPane scroll = new JScrollPane(rows);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(Color.WHITE);
        inventoryPanel.add(scroll, BorderLayout.CENTER);
        updateInventoryAlert();
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    /**
     * Creates an inventory row with the item name, category,
     * stock count, and a button to update its stock.
     */
    private JPanel createInventoryRow(final MenuItem item) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(12, 4, 12, 4)));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(item.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(NAVY);
        JLabel category = new JLabel(item.getCategory());
        category.setFont(new Font("SansSerif", Font.PLAIN, 11));
        category.setForeground(MUTED);
        details.add(name);
        details.add(Box.createVerticalStrut(4));
        details.add(category);
        row.add(details, BorderLayout.CENTER);

        JPanel stockControls = new JPanel(new BorderLayout(12, 0));
        stockControls.setOpaque(false);
        final JLabel quantity = new JLabel();
        quantity.setHorizontalAlignment(SwingConstants.RIGHT);
        quantity.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateInventoryQuantityLabel(quantity, item);
        stockControls.add(quantity, BorderLayout.CENTER);
        JButton update = new JButton("Update");
        update.setFont(new Font("SansSerif", Font.BOLD, 11));
        update.setForeground(BLUE);
        update.setFocusPainted(false);
        update.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String input = JOptionPane.showInputDialog(MilkTeaPOS.this,
                        "Enter the new stock quantity for " + item.getName() + ":",
                        String.valueOf(getStock(item)));
                if (input == null) {
                    return;
                }
                try {
                    int newStock = Integer.parseInt(input.trim());
                    if (newStock < 0) {
                        throw new NumberFormatException("Stock cannot be negative");
                    }
                    stockByItemId.put(item.getId(), Integer.valueOf(newStock));
                    refreshInventory();
                    refreshMenu();
                    refreshCart();
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(MilkTeaPOS.this,
                            "Enter a whole number that is zero or greater.",
                            "Invalid stock quantity", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        stockControls.add(update, BorderLayout.EAST);
        row.add(stockControls, BorderLayout.EAST);
        return row;
    }

    /** Updates the stock label and color based on the low-stock threshold. */
    private void updateInventoryQuantityLabel(JLabel quantity, MenuItem item) {
        int stock = getStock(item);
        if (stock <= LOW_STOCK_THRESHOLD) {
            quantity.setText("<html><span style='color:#BE2832'>" + stock
                    + " in stock &nbsp; LOW</span></html>");
            quantity.setForeground(ALERT_RED);
        } else {
            quantity.setText(stock + " in stock");
            quantity.setForeground(NAVY);
        }
    }

    /**
     * Recalculates and redraws the dashboard metrics and recent orders.
     */
    private void refreshDashboard() {
        dashboardPanel.removeAll();
        dashboardPanel.setOpaque(false);
        dashboardPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("A quick look at your JavaCup store.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);
        dashboardPanel.add(heading, BorderLayout.NORTH);

        JPanel dashboardContent = new JPanel(new BorderLayout(0, 16));
        dashboardContent.setOpaque(false);
        JPanel metrics = new JPanel(new GridLayout(1, 4, 12, 0));
        metrics.setOpaque(false);
        double salesTotal = 0;
        for (Sale sale : completedOrders) {
            salesTotal += sale.getTotal();
        }
        int cartQuantity = 0;
        for (CartItem item : cartItems) {
            cartQuantity += item.getQuantity();
        }
        metrics.add(createMetricCard("TOTAL SALES", CURRENCY.format(salesTotal),
                "Completed orders this session"));
        metrics.add(createMetricCard("COMPLETED ORDERS",
                String.valueOf(completedOrders.size()), "Paid orders this session"));
        metrics.add(createMetricCard("MENU ITEMS", String.valueOf(menuItems.size()),
                "Drinks, snacks, and add-ons"));
        metrics.add(createMetricCard("CURRENT ORDER", CURRENCY.format(getOrderTotal()),
                cartQuantity + (cartQuantity == 1 ? " item in cart" : " items in cart")));
        dashboardContent.add(metrics, BorderLayout.NORTH);
        dashboardContent.add(createRecentOrdersPanel(), BorderLayout.CENTER);
        dashboardPanel.add(dashboardContent, BorderLayout.CENTER);
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    /** Creates a dashboard card from a title, value, and description. */
    private JPanel createMetricCard(String labelText, String valueText, String detailText) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 15, 16, 15)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(MUTED);
        JLabel value = new JLabel(valueText);
        value.setFont(new Font("SansSerif", Font.BOLD, 22));
        value.setForeground(NAVY);
        JLabel detail = new JLabel(detailText);
        detail.setFont(new Font("SansSerif", Font.PLAIN, 10));
        detail.setForeground(MUTED);
        card.add(label);
        card.add(Box.createVerticalStrut(10));
        card.add(value);
        card.add(Box.createVerticalStrut(5));
        card.add(detail);
        return card;
    }

    /** Displays up to the five most recent completed orders. */
    private JPanel createRecentOrdersPanel() {
        JPanel recentOrders = new JPanel(new BorderLayout(0, 12));
        recentOrders.setBackground(Color.WHITE);
        recentOrders.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)));
        JLabel title = new JLabel("Recent orders");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(NAVY);
        recentOrders.add(title, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        if (completedOrders.isEmpty()) {
            JLabel empty = new JLabel("No completed orders yet. Your sales will appear here.");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 13));
            empty.setForeground(MUTED);
            rows.add(empty);
        } else {
            int shown = 0;
            for (int i = completedOrders.size() - 1; i >= 0 && shown < 5; i--, shown++) {
                Sale sale = completedOrders.get(i);
                JPanel row = new JPanel(new BorderLayout(12, 0));
                row.setOpaque(false);
                row.setBorder(new EmptyBorder(10, 0, 10, 0));
                JLabel details = new JLabel("<html><b>" + sale.getOrderType()
                        + "</b><br><span style='color:#697A91'>"
                        + sale.getCompletedAtText() + "</span></html>");
                details.setFont(new Font("SansSerif", Font.PLAIN, 12));
                JLabel total = new JLabel(CURRENCY.format(sale.getTotal()));
                total.setFont(new Font("SansSerif", Font.BOLD, 14));
                total.setForeground(NAVY);
                row.add(details, BorderLayout.CENTER);
                row.add(total, BorderLayout.EAST);
                rows.add(row);
                rows.add(new javax.swing.JSeparator());
            }
        }
        recentOrders.add(rows, BorderLayout.CENTER);
        return recentOrders;
    }

    /**
     * Builds the header with the logo, store name,
     * and a button to open the order history.
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(18, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, BLUE),
                new EmptyBorder(18, 19, 18, 19)));

        JLabel logo = new JLabel(loadImage("LOGO.jpg", 104, 78));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setOpaque(true);
        logo.setBackground(BLUE);
        logo.setPreferredSize(new Dimension(104, 78));

        JLabel title = new JLabel("JavaCup");
        title.setFont(new Font("SansSerif", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("MILK TEA  ·  FRUIT TEA  ·  MADE FRESH");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        subtitle.setForeground(new Color(190, 211, 237));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);

        JPanel brand = new JPanel(new BorderLayout(13, 0));
        brand.setOpaque(false);
        brand.add(logo, BorderLayout.WEST);
        brand.add(titles, BorderLayout.CENTER);
        header.add(brand, BorderLayout.WEST);

        JLabel openStatus = new JLabel("●  READY TO SERVE");
        openStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
        openStatus.setForeground(new Color(207, 226, 249));
        openStatus.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(openStatus, BorderLayout.CENTER);

        historyButton = new JButton();
        updateHistoryButton();
        historyButton.setPreferredSize(new Dimension(220, 60));
        historyButton.setMinimumSize(new Dimension(200, 56));
        historyButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        historyButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 210, 240), 1),
                new EmptyBorder(7, 13, 7, 13)));
        historyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        historyButton.setToolTipText("View completed orders");
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showOrderHistory();
            }
        });
        header.add(historyButton, BorderLayout.EAST);
        return header;
    }

    /** Updates the order-history button with the completed order count. */
    private void updateHistoryButton() {
        historyButton.setText("<html><div style='text-align:center'>ORDER HISTORY"
                + "<br><span style='font-weight:normal'>"
                + completedOrders.size() + " completed orders</span></div></html>");
        styleButton(historyButton, new Color(239, 246, 255), NAVY);
    }

    /**
     * Builds the menu area with a title, short instructions,
     * and a scrollable grid of product cards.
     */
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        JLabel menuTitle = new JLabel("Our menu");
        menuTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        menuTitle.setForeground(NAVY);
        controls.add(menuTitle);
        controls.add(Box.createVerticalStrut(4));
        JLabel description = new JLabel("Choose a drink or snack to start an order.");
        description.setFont(new Font("SansSerif", Font.PLAIN, 12));
        description.setForeground(MUTED);
        controls.add(description);
        panel.add(controls, BorderLayout.NORTH);

        menuGrid.setOpaque(false);
        menuGrid.setBorder(new EmptyBorder(2, 2, 10, 4));
        JScrollPane scroll = new JScrollPane(menuGrid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BACKGROUND);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Adds a category filter button.
     * Clicking it selects the category and displays matching products.
     */
    private void addFilterButton(JPanel parent, final String category) {
        JButton button = createSidebarButton(category);
        styleSidebarButton(button, category.equals(selectedCategory));
        categoryButtons.add(button);
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setPreferredSize(new Dimension(175, 38));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectedCategory = category;
                for (JButton filter : categoryButtons) {
                    styleSidebarButton(filter, filter.getText().equals(selectedCategory));
                }
                showPage("pos");
                refreshMenu();
            }
        });
        parent.add(button);
        parent.add(Box.createVerticalStrut(5));
    }

    /**
     * Builds the cart panel with the order type, cart items,
     * total price, and checkout button.
     */
    private JPanel buildOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(17, 16, 16, 16)));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel title = new JLabel("Current order");
        title.setFont(new Font("SansSerif", Font.BOLD, 19));
        title.setForeground(NAVY);
        JButton clearButton = new JButton("Clear");
        clearButton.setBorderPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setForeground(MUTED);
        clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!cartItems.isEmpty()) {
                    int answer = JOptionPane.showConfirmDialog(MilkTeaPOS.this,
                            "Remove all items from this order?", "Clear order",
                            JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        cartItems.clear();
                        refreshCart();
                    }
                }
            }
        });
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(clearButton, BorderLayout.EAST);
        heading.add(titleRow);
        heading.add(Box.createVerticalStrut(10));

        JLabel orderTypeLabel = new JLabel("ORDER TYPE");
        orderTypeLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        orderTypeLabel.setForeground(MUTED);
        heading.add(orderTypeLabel);
        heading.add(Box.createVerticalStrut(5));

        JPanel orderTypeButtons = new JPanel(new GridLayout(1, 2, 8, 0));
        orderTypeButtons.setOpaque(false);
        dineInButton = new JToggleButton("Dine in");
        takeOutButton = new JToggleButton("Take out");
        dineInButton.setUI(new BasicToggleButtonUI());
        takeOutButton.setUI(new BasicToggleButtonUI());
        ButtonGroup orderTypeGroup = new ButtonGroup();
        orderTypeGroup.add(dineInButton);
        orderTypeGroup.add(takeOutButton);
        takeOutButton.setSelected(true);
        dineInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectedOrderType = "Dine in";
                refreshOrderTypeButtons();
            }
        });
        takeOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectedOrderType = "Take out";
                refreshOrderTypeButtons();
            }
        });
        orderTypeButtons.add(dineInButton);
        orderTypeButtons.add(takeOutButton);
        heading.add(orderTypeButtons);
        refreshOrderTypeButtons();
        panel.add(heading, BorderLayout.NORTH);

        cartContents.setOpaque(false);
        cartContents.setLayout(new BoxLayout(cartContents, BoxLayout.Y_AXIS));
        JScrollPane cartScroll = new JScrollPane(cartContents);
        cartScroll.setBorder(BorderFactory.createEmptyBorder());
        cartScroll.getViewport().setBackground(Color.WHITE);
        panel.add(cartScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        JLabel note = new JLabel("Prices in Philippine pesos");
        note.setForeground(MUTED);
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bottom.add(note);
        bottom.add(Box.createVerticalStrut(8));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        JLabel totalCaption = new JLabel("TOTAL");
        totalCaption.setFont(new Font("SansSerif", Font.BOLD, 13));
        totalCaption.setForeground(MUTED);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        totalLabel.setForeground(NAVY);
        totalRow.add(totalCaption, BorderLayout.WEST);
        totalRow.add(totalLabel, BorderLayout.EAST);
        bottom.add(totalRow);
        bottom.add(Box.createVerticalStrut(13));

        JButton checkout = new JButton("CHECK OUT");
        checkout.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkout.setForeground(Color.WHITE);
        checkout.setBackground(BLUE);
        checkout.setFocusPainted(false);
        checkout.setBorderPainted(false);
        checkout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkout.setBorder(new EmptyBorder(13, 12, 13, 12));
        checkout.setAlignmentX(LEFT_ALIGNMENT);
        checkout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        checkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                checkout();
            }
        });
        bottom.add(checkout);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    /** Updates the Dine in and Take out button styles based on the selection. */
    private void refreshOrderTypeButtons() {
        styleOrderTypeButton(dineInButton, "Dine in".equals(selectedOrderType));
        styleOrderTypeButton(takeOutButton, "Take out".equals(selectedOrderType));
    }

    /** Applies the selected or unselected style to an order-type button. */
    private void styleOrderTypeButton(JToggleButton button, boolean selected) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setRolloverEnabled(false);
        button.setBackground(selected ? new Color(25, 52, 88) : new Color(45, 91, 151));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(17, 39, 69), selected ? 2 : 1),
                new EmptyBorder(10, 10, 10, 10)));
    }

    /**
     * Clears and redraws product cards for the selected category.
     * Add-ons are excluded because they are selected when customizing a drink.
     */
    private void refreshMenu() {
        menuGrid.removeAll();
        for (final MenuItem item : menuItems) {
            if (item instanceof AddOn) {
                continue;
            }
            if (!selectedCategory.equals("All") && !selectedCategory.equals(item.getCategory())) {
                continue;
            }
            menuGrid.add(createProductCard(item));
        }
        menuGrid.revalidate();
        menuGrid.repaint();
    }

    /**
     * Creates a product card with an image, category, name, price,
     * and Add button.
     */
    private JPanel createProductCard(final MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 10, 11, 10)));

        JLabel image = new JLabel(loadImage(item.getImageFile(), 180, 118));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setPreferredSize(new Dimension(180, 118));
        image.setOpaque(true);
        image.setBackground(PALE_BLUE);
        card.add(image, BorderLayout.NORTH);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        JLabel category = new JLabel(item.getCategory().toUpperCase());
        category.setFont(new Font("SansSerif", Font.BOLD, 10));
        category.setForeground(item.getCategoryColor());
        JLabel name = new JLabel(item.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 18));
        name.setForeground(NAVY);
        JLabel price = new JLabel(CURRENCY.format(item.getPrice()));
        price.setFont(new Font("SansSerif", Font.BOLD, 16));
        price.setForeground(BLUE);
        details.add(category);
        details.add(Box.createVerticalStrut(5));
        details.add(name);
        details.add(Box.createVerticalStrut(5));
        details.add(price);
        card.add(details, BorderLayout.CENTER);

        JButton add = new JButton("+ Add");
        add.setFont(new Font("SansSerif", Font.BOLD, 12));
        add.setForeground(Color.WHITE);
        add.setBackground(item.getCategoryColor());
        if (getStock(item) == 0) {
            add.setText("OUT OF STOCK");
            add.setBackground(MUTED);
            add.setEnabled(false);
        }
        add.setFocusPainted(false);
        add.setBorderPainted(false);
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.setBorder(new EmptyBorder(9, 10, 9, 10));
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                addToOrder(item);
            }
        });
        card.add(add, BorderLayout.SOUTH);
        return card;
    }

    /**
     * Loads an image from the project folder and scales it
     * to fit the requested dimensions.
     */
    private ImageIcon loadImage(String fileName, int width, int height) {
        File file = new File(fileName);
        if (!file.isFile()) {
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

    /**
     * Adds a product to the order.
     * For drinks, first collects the sugar level, cup size, add-ons, and quantity.
     * For snacks, checks stock and adds the item directly to the cart.
     */
    private void addToOrder(MenuItem item) {
        ArrayList<AddOn> chosenAddOns = new ArrayList<AddOn>();
        String sugarLevel = "N/A";
        String cupSize = "N/A";
        if (item instanceof MilkTea || item instanceof FruitTea) {
            JPanel choices = new JPanel();
            choices.setLayout(new BoxLayout(choices, BoxLayout.Y_AXIS));
            choices.add(new JLabel("Customize " + item.getName()));
            choices.add(Box.createVerticalStrut(8));

            JComboBox<String> sugarSelector = new JComboBox<String>(
                    new String[] {"0%", "25%", "50%", "75%", "100%"});
            sugarSelector.setSelectedItem("100%");
            JComboBox<String> sizeSelector = new JComboBox<String>(
                    new String[] {"12 oz", "16 oz"});
            sizeSelector.setSelectedItem("16 oz");
            JPanel sugarRow = new JPanel(new BorderLayout(12, 0));
            sugarRow.add(new JLabel("Sugar level"), BorderLayout.WEST);
            sugarRow.add(sugarSelector, BorderLayout.EAST);
            JPanel sizeRow = new JPanel(new BorderLayout(12, 0));
            sizeRow.add(new JLabel("Cup size"), BorderLayout.WEST);
            sizeRow.add(sizeSelector, BorderLayout.EAST);
            choices.add(sugarRow);
            choices.add(Box.createVerticalStrut(6));
            choices.add(sizeRow);
            choices.add(Box.createVerticalStrut(9));
            JLabel addOnLabel = new JLabel("ADD-ONS");
            addOnLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
            addOnLabel.setForeground(NAVY);
            choices.add(addOnLabel);
            ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
            for (AddOn addOn : availableAddOns) {
                JCheckBox checkBox = new JCheckBox(
                        addOn.getName() + "  (+" + CURRENCY.format(addOn.getPrice()) + ")");
                checkBoxes.add(checkBox);
                choices.add(checkBox);
            }
            JSpinner quantity = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
            JPanel quantityRow = new JPanel(new BorderLayout(8, 0));
            quantityRow.add(new JLabel("Quantity"), BorderLayout.WEST);
            quantityRow.add(quantity, BorderLayout.EAST);
            choices.add(Box.createVerticalStrut(8));
            choices.add(quantityRow);

            int answer = JOptionPane.showConfirmDialog(this, choices, "Customize drink",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (answer != JOptionPane.OK_OPTION) {
                return;
            }
            sugarLevel = (String) sugarSelector.getSelectedItem();
            cupSize = (String) sizeSelector.getSelectedItem();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    chosenAddOns.add(availableAddOns.get(i));
                }
            }
            int count = ((Integer) quantity.getValue()).intValue();
            if (!canReserveStock(item, chosenAddOns, count)) {
                showInsufficientStock(item, chosenAddOns, count);
                return;
            }
            for (int i = 0; i < count; i++) {
                addCartItem(item, chosenAddOns, sugarLevel, cupSize);
            }
        } else {
            if (!canReserveStock(item, chosenAddOns, 1)) {
                showInsufficientStock(item, chosenAddOns, 1);
                return;
            }
            addCartItem(item, chosenAddOns, sugarLevel, cupSize);
        }
        refreshCart();
    }

    /**
     * Combines an item with an existing cart row when its product
     * and customizations match; otherwise, creates a new row.
     */
    private void addCartItem(MenuItem item, List<AddOn> addOns,
            String sugarLevel, String cupSize) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.matches(item, addOns, sugarLevel, cupSize)) {
                cartItem.increaseQuantity();
                return;
            }
        }
        cartItems.add(new CartItem(item, addOns, sugarLevel, cupSize));
    }

    /**
     * Checks that enough unreserved stock is available for the product
     * and all selected add-ons.
     */
    private boolean canReserveStock(MenuItem item, List<AddOn> addOns, int quantity) {
        if (getStock(item) - getReservedStock(item.getId()) < quantity) {
            return false;
        }
        for (AddOn addOn : addOns) {
            if (getStock(addOn) - getReservedStock(addOn.getId()) < quantity) {
                return false;
            }
        }
        return true;
    }

    /** Counts how many units of an item are already reserved in the cart. */
    private int getReservedStock(String itemId) {
        int reserved = 0;
        for (CartItem cartItem : cartItems) {
            if (itemId.equals(cartItem.getProduct().getId())) {
                reserved += cartItem.getQuantity();
            }
            for (AddOn addOn : cartItem.getAddOns()) {
                if (itemId.equals(addOn.getId())) {
                    reserved += cartItem.getQuantity();
                }
            }
        }
        return reserved;
    }

    /** Shows which product or add-on does not have enough stock. */
    private void showInsufficientStock(MenuItem item, List<AddOn> addOns, int quantity) {
        StringBuilder message = new StringBuilder("Not enough stock for ")
                .append(item.getName()).append(" (").append(quantity).append(" requested).");
        for (AddOn addOn : addOns) {
            if (getStock(addOn) - getReservedStock(addOn.getId()) < quantity) {
                message.append("\n").append(addOn.getName()).append(": ")
                        .append(Math.max(0, getStock(addOn) - getReservedStock(addOn.getId())))
                        .append(" available.");
            }
        }
        if (getStock(item) - getReservedStock(item.getId()) < quantity) {
            message.append("\n").append(item.getName()).append(": ")
                    .append(Math.max(0, getStock(item) - getReservedStock(item.getId())))
                    .append(" available.");
        }
        JOptionPane.showMessageDialog(this, message.toString(),
                "Insufficient stock", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Redraws the cart, shows an empty-cart message when needed,
     * and updates the total price.
     */
    private void refreshCart() {
        cartContents.removeAll();
        if (cartItems.isEmpty()) {
            JLabel empty = new JLabel("<html><div style='text-align:center'>Your order is empty.<br>"
                    + "Add a drink to get started!</div></html>");
            empty.setForeground(MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(28, 4, 20, 4));
            cartContents.add(empty);
        } else {
            for (int i = 0; i < cartItems.size(); i++) {
                cartContents.add(createCartRow(cartItems.get(i)));
                cartContents.add(Box.createVerticalStrut(9));
            }
        }
        totalLabel.setText(CURRENCY.format(getOrderTotal()));
        cartContents.revalidate();
        cartContents.repaint();
    }

    /**
     * Creates a cart row with the item name, customizations, subtotal,
     * quantity controls, and a remove button.
     */
    private JPanel createCartRow(final CartItem cartItem) {
        JPanel row = new JPanel(new BorderLayout(8, 4));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        JPanel description = new JPanel();
        description.setOpaque(false);
        description.setLayout(new BoxLayout(description, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(cartItem.getProduct().getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 13));
        name.setForeground(NAVY);
        JLabel options = new JLabel(cartItem.getCustomizationText());
        options.setFont(new Font("SansSerif", Font.PLAIN, 10));
        options.setForeground(MUTED);
        JLabel price = new JLabel(CURRENCY.format(cartItem.getSubtotal()));
        price.setFont(new Font("SansSerif", Font.BOLD, 12));
        price.setForeground(NAVY);
        description.add(name);
        description.add(Box.createVerticalStrut(3));
        description.add(options);
        description.add(Box.createVerticalStrut(4));
        description.add(price);
        row.add(description, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setOpaque(false);
        JButton minus = smallQuantityButton("−");
        JButton plus = smallQuantityButton("+");
        JLabel quantity = new JLabel(String.valueOf(cartItem.getQuantity()));
        quantity.setFont(new Font("SansSerif", Font.BOLD, 12));
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (cartItem.getQuantity() == 1) {
                    cartItems.remove(cartItem);
                } else {
                    cartItem.decreaseQuantity();
                }
                refreshCart();
            }
        });
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (canReserveStock(cartItem.getProduct(), cartItem.getAddOns(), 1)) {
                    cartItem.increaseQuantity();
                    refreshCart();
                } else {
                    showInsufficientStock(cartItem.getProduct(), cartItem.getAddOns(), 1);
                }
            }
        });
        controls.add(minus);
        controls.add(quantity);
        controls.add(plus);
        JButton remove = new JButton("×");
        remove.setToolTipText("Remove this item from the order");
        remove.getAccessibleContext().setAccessibleName("Remove " + cartItem.getProduct().getName());
        remove.setFont(new Font("SansSerif", Font.BOLD, 16));
        remove.setMargin(new Insets(2, 7, 2, 7));
        remove.setFocusPainted(false);
        remove.setBorderPainted(false);
        remove.setBackground(new Color(235, 242, 251));
        remove.setForeground(new Color(51, 91, 143));
        remove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                cartItems.remove(cartItem);
                refreshCart();
            }
        });
        controls.add(remove);
        row.add(controls, BorderLayout.EAST);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(7, 0, 9, 0)));
        return row;
    }

    /** Creates a small button for increasing or decreasing quantity. */
    private JButton smallQuantityButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setMargin(new Insets(2, 7, 2, 7));
        button.setFocusPainted(false);
        button.setForeground(NAVY);
        return button;
    }

    /** Adds together the subtotals of all cart items. */
    private double getOrderTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Checks the cart and stock, accepts payment,
     * creates a receipt, and completes the order.
     */
    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one item before checking out.",
                    "Empty order", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!orderFitsCurrentStock()) {
            JOptionPane.showMessageDialog(this,
                    "The order exceeds current stock. Update the order or inventory before checkout.",
                    "Insufficient stock", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = getOrderTotal();
        while (true) {
            String input = JOptionPane.showInputDialog(this,
                    "Amount due: " + CURRENCY.format(total) + "\nEnter cash received:",
                    "Cash payment", JOptionPane.PLAIN_MESSAGE);
            if (input == null) {
                return;
            }
            double cash;
            try {
                cash = Double.parseDouble(input.trim().replace(",", ""));
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Enter a valid cash amount.",
                        "Invalid amount", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (cash < total) {
                JOptionPane.showMessageDialog(this, "Cash received is less than the total.",
                        "Insufficient payment", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            String receipt = createReceipt(total, cash);
            deductOrderStock();
            completedOrders.add(new Sale(receipt, total, selectedOrderType));
            updateHistoryButton();
            JOptionPane.showMessageDialog(this, receipt, "Payment complete",
                    JOptionPane.INFORMATION_MESSAGE);
            cartItems.clear();
            nextReceiptNumber++;
            refreshCart();
            refreshDashboard();
            refreshInventory();
            refreshMenu();
            return;
        }
    }

    /** Confirms that the reserved order does not exceed current stock. */
    private boolean orderFitsCurrentStock() {
        for (MenuItem item : menuItems) {
            if (getReservedStock(item.getId()) > getStock(item)) {
                return false;
            }
        }
        return true;
    }

    /** Deducts the ordered products and selected add-ons from inventory. */
    private void deductOrderStock() {
        for (CartItem cartItem : cartItems) {
            String productId = cartItem.getProduct().getId();
            stockByItemId.put(productId, Integer.valueOf(
                    getStock(cartItem.getProduct()) - cartItem.getQuantity()));
            for (AddOn addOn : cartItem.getAddOns()) {
                stockByItemId.put(addOn.getId(), Integer.valueOf(
                        getStock(addOn) - cartItem.getQuantity()));
            }
        }
    }

    /**
     * Creates a text receipt listing the items, total,
     * cash received, and change.
     */
    private String createReceipt(double total, double cash) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("          JAVACUP\n");
        receipt.append("     MILK TEA & FRUIT TEA\n");
        receipt.append("--------------------------------\n");
        receipt.append(String.format(Locale.US, "Receipt #%04d%n", nextReceiptNumber));
        receipt.append("Order type: ").append(selectedOrderType).append('\n');
        receipt.append("--------------------------------\n");
        for (CartItem item : cartItems) {
            receipt.append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity()).append('\n');
            if (!"N/A".equals(item.getCupSize())) {
                receipt.append("  ").append(item.getCupSize())
                        .append(" · Sugar ").append(item.getSugarLevel()).append('\n');
            }
            if (!item.getOptionsText().isEmpty()) {
                receipt.append("  ").append(item.getOptionsText()).append('\n');
            }
            receipt.append("  ").append(CURRENCY.format(item.getSubtotal())).append('\n');
        }
        receipt.append("--------------------------------\n");
        receipt.append("TOTAL:    ").append(CURRENCY.format(total)).append('\n');
        receipt.append("CASH:     ").append(CURRENCY.format(cash)).append('\n');
        receipt.append("CHANGE:   ").append(CURRENCY.format(cash - total)).append('\n');
        receipt.append("--------------------------------\n");
        receipt.append("       Salamat po!");
        return receipt.toString();
    }

    /** Displays the orders completed during the current session. */
    private void showOrderHistory() {
        if (completedOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No completed orders yet.",
                    "Order history", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder history = new StringBuilder();
        history.append("Completed orders: ").append(completedOrders.size()).append("\n\n");
        for (Sale sale : completedOrders) {
            history.append(sale.getCompletedAtText())
                    .append("  —  ")
                    .append(sale.getOrderType())
                    .append("  —  ")
                    .append(CURRENCY.format(sale.getTotal()))
                    .append('\n');
        }
        JTextArea textArea = new JTextArea(history.toString(), 12, 34);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                "Order history", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Applies consistent background and foreground colors to a button. */
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
}
