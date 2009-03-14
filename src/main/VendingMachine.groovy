/*
 * author: Matthew Taylor
 */
class VendingMachine {

    static final COINS = [
            'dollar': 1,
            'quarter': 0.25,
            'dime': 0.1,
            'nickel': 0.05
    ]

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

    def methodMissing(String name, args) {
        println "methodMissing: $name"
        if (name.startsWith('get')) {
            this.vend(name - 'get')
        } else {
            VendingMachine.metaClass."$name" = { ->
                println "in $name"
                delegate.pay(COINS[name])
            }
            return this.pay(COINS[name])
        }
    }
}