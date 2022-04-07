package scripts;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordList;

import java.util.*;

public class searcher {
    public double innerProduct(HashMap indexMap, String id, KeywordList kl) {
        double result = 0.0;

        for (Keyword keyword : kl) {
            String term = keyword.getString();
            double weightQuery = keyword.getCnt();

            if (!indexMap.containsKey(term)) {
                continue;
            }

            HashMap weightMap = (HashMap) indexMap.get(term);
            double weightDocument = (double) weightMap.get(id);

            double multiply = weightQuery * weightDocument;
            result += multiply;
        }

        return result;
    }
}
