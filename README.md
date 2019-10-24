First you need to create a Transitioner object containing your original and ending views:

In Java
```java
Transitioner transition = new Transitioner(
                   original_view, ending_view,                              
                   new Transitioner.Callback() {
                       @Override
                       public void onPercentChanged(float percent) {                                       
                           // to do thing with percent change
                       }
                   });
```
In Kotlin
```kotlin
val transition = Transitioner(original_view, ending_view)
```
The view pairs must have matching "tag" attributes so that they can be bound together:
```xml
<ConstraintLayout
        android:id="@+id/original_view"
        android:tag="constrView"
        ...>

        <TextView
            android:id="@+id/text"
            android:tag="firstView"
            .../>
</ConstraintLayout>

<ConstraintLayout
        android:id="@+id/ending_view"
        android:tag="constrView"
        android:visibility="invisible"
        ...>

        <EditText
            android:id="@+id/text3"
            android:tag="firstView"
            .../>
 </ConstraintLayout>
```
I recommend you hide the second view layout, since it's only used as a placeholder for the end destination.
The views can be of any type, you can mix and match them, the two layouts can have a different number of views and nested layouts are 100% supported. The only things to keep in mind are:

-  All views which you would want to match together must have the same tag attribute in both layouts

-  All unmatched views will remain at their original place inside the original layout

-  The second layout is just a placeholder. It doesn't hold any logic, it only shows where the original layout should move to.

### Additional methods and tweaks
In Java
```java
transition.setDuration(500);

transition.setInterpolator(new AccelerateDecelerateInterpolator());

transitioner.animateTo(0f, 500, new BounceInterpolator());
```
In Kotlin
```kotlin
transition.duration = 500

transition.interpolator = AccelerateDecelerateInterpolator()

transition.animateTo(percent = 0f)

transition.onProgressChanged {
//triggered on every progress change of the transition
    seekBar.progress = (it * 100).toInt()
}    
    
val progress: Float = transition.currentProgress
```
