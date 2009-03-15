/*
 * author: Matthew Taylor
 */
class InventoryLoader {

    def vendor

    public InventoryLoader(vendor) {
        this.vendor = vendor
    }

    void load(inventory) {
        vendor.inventory = [:]
        inventory.trim().eachLine { line ->
            def item = line.split(',')
            vendor.inventory."${item[0]}" = [
                    name: item[1],
                    price: item[2].toDouble(),
                    quantity: item[3].toInteger()
            ]
        }
    }

    void loadBank(bank) {
        vendor.bank = [:]
        bank.trim().eachLine { line ->
            def item = line.split(':')
            vendor.bank[Coin."${(item[0] - 's')}"] = item[1].toInteger()
        }
    }
}