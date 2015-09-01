Hugo
====

Annotation-triggered method call logging (and insults) for your debug builds.

As a programmer, you often add log statements to print method calls, their arguments, their return
values, and the time it took to execute. This is not a question. Every one of you does this.
Shouldn't it be easier?

Simply add `@DebugLog` to your methods and you will automatically get all of the things listed above
logged for free. In addition, the logs will include random insults, because why aren't you using the debugger?

```java
@Twig
public String getName(String first, String last) {
  SystemClock.sleep(15); // Always do this! #PerfMatters
  return first + " " + last;
}
```
```
V/Example: ⇢ getName(first="Matthew", last="Compton")
V/Example: ⇠ getName [insult] = "You're a disappointment."
V/Example: ⇠ getName [16ms] = "Matthew Compton"
```
```
V/Example: ⇢ getName(first="Jake", last="Wharton")
V/Example: ⇠ getName [insult] = "I can't believe you're using enums."
V/Example: ⇠ getName [16ms] = "Jake Wharton"
```

The logging will only happen in debug builds and the annotation itself is never present in the
compiled class file for any build type. This means you can keep the annotation and check it into
source control. It has zero effect on non-debug builds.

Add it to your project today!

```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath 'com.ambergleam.twig:twig-plugin:1.0.0'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.ambergleam.twig'
```

License
--------

    Copyright 2015 Matthew Compton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
