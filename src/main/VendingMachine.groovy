/*
 * author: Matthew Taylor
 */
class VendingMachine {

    def inventory
    def deposit = 0
    
    def pay(amount) {
        deposit += amount
        deposit
    }

    def vend(code) {
        def item = inventory[code]
        if (!item) return "Sorry, no code of '${code}' exists."
        if (item.quantity <= 0) return "Sorry, no more ${item.item.name}, please choose again."
        if (item.price <= deposit) {
            deposit = deposit - item.price
            item.quantity--
            return item.item
        }
    }

    def change() {
        def change = deposit
        deposit = 0
        change
    }
}