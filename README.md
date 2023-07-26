# Tur-lang
my very own coding language, this language emulates a Turing Machine [with my own syntax]

# Run the language:

To run a script you need to use the "***TurCompiler.java***" file. Althought the name of the file says compiler really it is an interpreter.

After running the script you will be prompted for the path to the script you wrote, this path is the absolute path. After entering it you will be able to run your code, see the initial turing tape and the final turing tape (after changes).

the project comes with to example scripts,

1. multiplier.txt -  a turing machine that multiplies two uniary numbers and writes the result at the end of the turing tape.
2. BinAnd.txt - a turing machine that does the binary and operation on two binary numbers and writes the result at the end of the turing tape.

# Syntax Explanation:

## Node Syntax:
The syntax works like nodes in a turing machine model.

### 1. line decleration
The node starts with a line decleration, a line tag. In other words a name for the current node your working on.

    /**any line tag**/ - line declaration.

  _In a more general way this just declares a break point in the code, you can call back to it to jump to this part of the script (and run it)._
### 2. first character - in
As with a turing machine node,
The first character is the character that is compared to the current position on the turing tape.
meaning that if it is equal, the code for the current node is executed, if not it continues forward.

    | - the start of the tape,

    ? - any character,
    
    ^ - empty character on tape.
### 3. second character - alt
The second character is the new character for the current position in the turing tape, the substitution.

    ? - stay the same value that the character was previously,

    ^ - empty character on tape.

    ** cannot be "|"
### 4. third charcter - dir
The third character is the direction in which we move on the turing tape.

    < - is move left,
    
    \> - is move right,
    
    ? - is stay in place.
### 5. last character - function
The final charcter is the action character, this is where an action happens which in this case means either stop the program
or jump to another part of it.

    e - exit code (finish running current script),

    ? - do nothing, 

    j(**any line tag**) - jump to line tag in code and start executing that part of the code.

_If nothing is done the script run just continues, meaning if it gets to the end of the line and no function is called the code continues to the next line._
## What it looks like:

All in all a line of code is supposed to look like

|line decleration| in | alt | dir | function |
|---|---|---|---|---|
|/1/|a|b|>|j(10)|
|/10/|b|a|?|e|

The cool thing here is that you can chain a chunk of code.this is made to imulate multiple rules for a node in a turing machine 

    /1/ ab>j(10) ba<j(9)

If the tape is somthing like "|b", the first command "***ab>j(10)***" is skipped because a!=b, so the "***ba<j(9)***" code is run instead

## File structure

The general file structure is:
    
    IN[**any value that you want**]
    
    **after initializing the tape comes the code**

    /1/ ab>j(10) ba<j(9)
    /9/ ba<j(1)
    /10/ ba?e
IN[] - initializing the turing tape (the first character "|" is automatically placed first before enething entered in between the []. Everything after the values in between [] is automatically ^, meaning an empty character.


# Simple example

The main point of the language is to take turing machine models and to run them and write them out like computer code.

Lets take a simple turing machine and turn it into tur-lang code.

### This is the model I chose:
![image](https://github.com/silb-danny/Tur-lang/assets/111777573/bedef41e-d45a-4326-b154-1ebe51e3d9cd)

the initial tape will be "***|baa***"
## The code:
    IN[baa]

    /1/ bb>j(1) aa>j(2)
    /2/ bb>j(1) aa>e

Now try running this code and check if running the model gives the same result.


As you can see translating the model to code isn't very difficult the only thing to keep in mind is the order of the lines (nodes) - it should be similar to the model.

## Afterthoughts
Although not all functionalities of a turing machine are present (if word not in language - not accepting) you can find workarounds to make these models work.

    
