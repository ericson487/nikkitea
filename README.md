# JavaCup Point of Sale

A blue-themed Java Swing point-of-sale app with a prominent JavaCup brand and
LOGO.jpg header image. It includes image-based milk tea
and fruit tea menus, drink sugar levels (0%, 25%, 50%, 75%, and 100%), 12 oz / 16 oz
cup sizes, a sidebar dashboard and menu-category navigation, add-on selection, cart
quantity controls and item removal, Dine in / Take out order types, cash checkout
with change, receipts, recent sales, and completed-order history.
The app opens to a login screen; use `Dream Team2.0` as the username and `JAVACUP`
as the password.

Both cup sizes currently use the listed menu price.
Dashboard sales and order metrics include completed orders from the current app session.
The Inventory page in the sidebar tracks menu and add-on quantities. Items start with
20 units for the current session, trigger a red low-stock alert at 5 units or fewer,
and are deducted when an order is paid. Use Update on the Inventory page to set a
quantity; items with no stock cannot be added to an order.
    
## Run

Keep the `.java` files and `.jpg` images, including `LOGO.jpg`, in this folder. From this folder,
compile and launch the app:

```powershell
javac *.java
java Amain
```

The menu is held in an `ArrayList`; the active cart and completed orders use
`LinkedList`. These are in-memory collections, so orders are cleared when the app
closes.

## OOP concepts used

- **Encapsulation:** model fields are private and accessed through methods.
- **Inheritance:** `MilkTea`, `FruitTea`, and `AddOn` extend `MenuItem`.
- **Abstraction:** `MenuItem` is an abstract base class.
- **Polymorphism:** menu items provide their own category and category color.
