package Tur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class TurCompiler {
    public static Scanner sc = new Scanner(System.in);
    public static String[] errM = {
            "0file does not exist",
            "1unreadable file",
            "2line declaration repeat",
            "3incorrect input declaration",
            "4stupid characters -- line declaration",
            "5stupid characters -- in (code)",
            "6stupid characters -- alternate (code)",
            "7stupid characters -- direction (code)",
            "8stupid characters -- function (code)",
            "9out of bounds input (| character exception)"
    };
    public static void errorMessage(int code) {
        // the function gets an error code
        // the function prints the error and stops the code
        System.err.println(errM[code]);
        System.exit(0);
    }
    public static void errorWarning(int code) {
        // the function gets an error code
        // the function prints the error
        System.err.println(errM[code]);
    }
    public static File getFile() {
        // the function gets a file and returns a file object
        System.out.println("please enter code filepath");
        String filename = sc.nextLine();
        File prog = new File(filename);
        if(!prog.exists()) {
            errorMessage(0);
            return null;
        }
        if(!prog.canRead()) {
            errorMessage(1);
            return null;
        }
        return prog;
    }
    public static String[] parseFile(File prog, Hashtable<String,Integer> lne) throws FileNotFoundException {
        // the function gets a "code" file, and a hashtable pointer
        // the function changes hashtable pointer to a hashtable that points to every new line (with id)

        /* reading file */
        Scanner fileStr = new Scanner(prog);
        System.out.print(".");

        /* making string */
        String str = "";
        while (fileStr.hasNextLine())
            str += fileStr.nextLine();
        str = str.trim();
        System.out.print(".");

        /* spliting input and code */
        String[] data = str.split("]");
        if(data[0].isEmpty())
            data = new String[]{"", data[1]};
        if(data[1].length()>1 && (data[1].substring(0,2).equals("IN") || data[1].substring(0,3).equals("IN[")))
            errorMessage(3);
        fileStr.close();
        System.out.print(".");

        /* removing whitespaces */
        data[0] = data[0].substring(3).replaceAll("\\s+","");
        data[1] = data[1].replaceAll("\\s+","");
        System.out.print(".");

        /* pointers */
        int ind = 0;
        boolean dec = false;
        String newData = "";
        String stDec = "";
        System.out.print(".");
        for (char c: data[1].toCharArray()) {
            if(!dec) {
                if (c == '/') {
                    dec = true;
                }
                else {
                    ind++;
                    newData += c;
                }
            } else if (dec) {
                if (c == '/') {
                    if (stDec.length() == 0) {
                        System.out.println("");
                        errorMessage(4);
                    }
                    if(lne.containsKey(stDec)) {
                        System.out.println("");
                        errorMessage(2);
                    }
                    lne.put(stDec,ind);
                    stDec = "";
                    dec = false;
                }
                else {
                    stDec += c;
                }
            }
        }
        data[1] = newData;
        System.out.println("");
        return data;
    }
    public static String run(String input,String code, Hashtable<String,Integer> flags,boolean debug) {
        // the function runs the code and changes the input accordingly
        // the function gets input code and code flags (indexes)
        int cInd = 0, iInd = 1;
        boolean act;
        int dir;
        String func;
        while (cInd < code.length()) {
            act = checkIn(code.charAt(cInd),input.charAt(iInd));
            if(debug)
                System.out.println(code.charAt(cInd)+" "+input.charAt(iInd)+" "+act);
            cInd ++;
            input = (act)?checkAlt(code.charAt(cInd),input,iInd):input;
            if(debug)
                System.out.println(code.charAt(cInd)+" "+input.charAt(iInd)+" "+input);
            cInd ++;
            dir = (act)?checkDir(code.charAt(cInd)):0;
            if(debug)
                System.out.println(code.charAt(cInd)+" "+input.charAt(iInd)+" "+dir);
            cInd ++;
            if(iInd+1 == input.length() && dir == 1)
                input += '^';
            if(iInd+dir < 0)
                errorWarning(9);
            iInd = Math.max(0,iInd+dir);
            func = checkFunc(code.charAt(cInd),code,cInd);
            if(debug)
                System.out.println(cInd+" "+code.charAt(cInd)+" "+input.charAt(iInd)+" "+func);
            switch (func.charAt(0)){
                case '1':
                    cInd = (act)?flags.get(func.substring(1)):code.indexOf(")",cInd)+1;
                    break;
                case '2':
                    if(act)
                        return input;
                    else
                        cInd ++;
                    break;
                case '0':
                    cInd ++;
                    break;
            }
            if(debug)
                System.out.println(cInd+" "+code.charAt(cInd));
        }
        return input;
    }
    public static boolean checkIn(char in, char strip){
        return (in == '?') || (in == strip);
    }
    public static String checkAlt(char alt, String input, int ind){
        if(alt == '?')
            return input;
        if (input.charAt(ind) != '|')
            return input.substring(0, ind) + alt + input.substring(ind + 1);
        if ((alt == '|' && input.charAt(ind) != '|') || (alt != '|' && input.charAt(ind) == '|')) {
            errorWarning(6);
        }
        return input;
    }
    public static int checkDir(char dir){
        int d = 0;
        switch (dir){
            case '<':
                d=-1;
                break;
            case '>':
                d=1;
                break;
            case '?':
                d=0;
                break;
            default:
                System.out.println(dir);
                errorWarning(7);
                break;
        }
        return d;
    }
    public static String checkFunc(char func, String code, int ind){
        String act = "";
        switch (func){
            case 'j':
                act += "1";
                act += code.substring(ind+2,code.indexOf(")",ind));
                break;
            case 'e':
                act += "2";
                break;
            case '?': // nothing
                act += "0";
                break;
            default: // nothing
                act += "0";
                errorWarning(8);
                break;
        }
        return act;
    }
    public static void main(String[] args) throws FileNotFoundException {
        // getting file
        File prog = getFile();
        System.out.println("file length: "+prog.length());
        System.out.println("print debugging? (1 yes /0 no)");
        boolean debug = sc.nextInt() != 0;
        // parsing commands and input
        Hashtable<String,Integer> flags = new Hashtable<>();
        System.out.print("parsing");
        String[] data = parseFile(prog,flags);
        String input = "|"+data[0]+"^^";
        String code = data[1];
        System.out.println("finished parsing");
        System.out.println("-----");
        System.out.println("inp:"+input);
        System.out.println("-----");
        if(debug)
            System.out.println("code:"+code);
        // code run
        String out = run(input,code,flags,debug);
        // printing output
        System.out.println("out:"+out);
        System.exit(0);
    }
}
