package yan.ycc.api.abc;

import yan.foundation.driver.lang.Phase;
import yan.ycc.api.YCTree;

public abstract class AbstractCSAnalyzer extends Phase<YCTree.TranslationUnit, YCTree.TranslationUnit>
        implements YCTree.VoidVisitor {
    @Override
    public YCTree.TranslationUnit transform(YCTree.TranslationUnit translationUnit) {
        translationUnit.accept(this);
        return translationUnit;
    }
}
