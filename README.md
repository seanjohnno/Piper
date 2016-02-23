### Piper

Piper is a simple reactive library, allowing you to chain together 'Pipes', passing the output of one pipe as input into the next

#### Example

```Java
// Sample data
String[] data = new String[] { "Lets", "pass", "a", "bunch", "of", "strings", "in", "and", "manipulate", "them" };

// Starts the pipe chain
Piper.<String[]>start(data)

  // Splits array and passed each item separately
  .connect(Piper.<String>splitArr())

  // Transforms each string to uppercase
  .connect(Piper.<String, String>transform(new Piper.Func1<String, String>() {
      @Override
      public String call(String input) {
          return input.toUpperCase();
      }
  }))

  // Sort the words and output the sorted list
  .connect(Piper.sort(String.CASE_INSENSITIVE_ORDER))

  // Split them again
  .connect(Piper.<String>split())
  
  // Do something with each word
  .connect(new Pipe<String, Void>() {
      @Override
      public void handleInput(String input) {
         // Do something
      }
  });
```

#### Threading

You can choose the thread the pipe handles the input on.

*The main thread:*

```Java
... .connect(new Pipe<String, Void>(Pipe.ThreadType.ThreadMain) {
    @Override
    public void handleInput(String input) {
       // Do something on main thread
    }
  });
```

*An IO thread:*

```Java
.connect(new Pipe<String, Void>(Pipe.ThreadType.IOThread) {
    @Override
    public void handleInput(String input) {
        // Lon running task e.g. Send a http request
    }
});
```

*You can also create your own executor:*

```Java
.connect(new Pipe<String, Void>(Pipe.ThreadType.IOThread) {
    @Override
    public void handleInput(String input) {
        // Lon running task e.g. Send a http request
    }
    
    @Override
    protected Executor getIOExecutor() {
        return /*Your executor*/;
    }
});
```




