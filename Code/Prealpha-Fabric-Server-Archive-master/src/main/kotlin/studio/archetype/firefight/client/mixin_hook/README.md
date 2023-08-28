The classes in this package allow us to call Kotlin code in mixins client side,
because Java kinda sucks, and I'd rather just proxy everything over to Kotlin
via JvmStatic annotations and do our heavy lifting work from the Kotlin code.