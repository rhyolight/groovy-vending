enum Coin {

    dollar(1), quarter(0.25), dime(0.1), nickel(0.05)

    Coin(value) { this.value = value }

    private final value

    def value() { return value }
}