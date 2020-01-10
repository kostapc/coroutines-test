# kotlin-coroutines-test

just testing different behaviors

## direct call - without wrapper
```
using direct call
async vars
after async calls = 204
value: val one
value: val two
value: val three
after await time: 3022
exec time: 3226


sync vars
after sync calls = 3007
value: val one
value: val two
value: val three
after print time: 0
exec time: 3008


asyncAndAwaitArr
result: val 1
result: val 2
result: val 3
exec time: 3014


launchAndJoinArr
result: val 2
result: val 1
result: val 3
exec time: 1027
```

## asyncJavaCall - wrapped with withContext(Dispatchers.IO)
```$text
using asyncJavaCall
async vars
after async calls = 180
value: val one
value: val two
value: val three
after await time: 1069
exec time: 1251


sync vars
after sync calls = 3006
value: val one
value: val two
value: val three
after print time: 0
exec time: 3006


asyncAndAwaitArr
result: val 1
result: val 2
result: val 3
exec time: 1010


launchAndJoinArr
result: val 3
result: val 2
result: val 1
exec time: 1009
```

## realAsyncJavaCall - wrapped with using thread pool
```$text
using realAsyncJavaCall
async vars
after async calls = 138
value: val one
value: val two
value: val three
after await time: 1049
exec time: 1188


sync vars
after sync calls = 3011
value: val one
value: val two
value: val three
after print time: 0
exec time: 3012


asyncAndAwaitArr
result: val 1
result: val 2
result: val 3
exec time: 1009


launchAndJoinArr
result: val 2
result: val 1
result: val 3
exec time: 1007
```