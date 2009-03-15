/*
 * author: Matthew Taylor
 */
class InventoryLoadingTests extends GroovyTestCase {

    void testLoadInventory() {
        def inventory = """
A1,Baby Ruth,1.00,34
A2,Zagnut,1.00,22
A3,Wrigley's Gun,0.45,120
A4,Snickers,0.75,40
B1,Skittles,1.00,10
B2,Cheetos,1.25,5
B3,Doritos,1.25,10
B4,Honey Bun,1.50,7
        """
        def vendor = new VendingMachine()
        def loader = new InventoryLoader(vendor)
        loader.load(inventory)
        assertNotNull vendor.inventory
        assertEquals 1, vendor.inventory.A1.price
        assertEquals 'Zagnut', vendor.inventory.A2.item.name
        assertEquals 120, vendor.inventory.A3.quantity
    }

    void testLoadBank() {
        def bank = """
dollars:100
quarters:101
dimes:102
nickels:103
        """
        def vendor = new VendingMachine()
        def loader = new InventoryLoader(vendor)
        loader.loadBank(bank)
        assertNotNull vendor.bank
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 101, vendor.bank[Coin.quarter]
        assertEquals 102, vendor.bank[Coin.dime]
        assertEquals 103, vendor.bank[Coin.nickel]
    }
}