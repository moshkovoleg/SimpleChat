//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JavaOnlyFilter implements ChatFilter {
    List<String> censoredList = new LinkedList();

    public JavaOnlyFilter() {
        this.censoredList.add("Путин");
        this.censoredList.add("Война");
    }

    public String filter(String message) {
        String word;
        for(Iterator var2 = this.censoredList.iterator(); var2.hasNext(); message = message.replaceAll(word, "JAVA")) {
            word = (String)var2.next();
        }

        return message;
    }
}
