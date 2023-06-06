# Zxcvbn

TODO:
- Finish implementing Unit testing
- Implement Android test
- Add example in MainActivity

1. Add it in your root build.gradle at the end of repositories:
```kotlin
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2. Add the dependency
```kotlin
dependencies {
	implementation 'com.github.slaviboy:Zxcvbn:0.0.1'
}
```

## OLD PROBLEM WITH JITPACK
> Task :zxcvbn:compileDebugKotlin FAILED

Fixed by removing multiple `res` folders, regitered in build gradle with [commit](https://github.com/slaviboy/Zxcvbn/commit/351ad2a47cd6f5dd1581339a4c8617b6d8b31a3f)
