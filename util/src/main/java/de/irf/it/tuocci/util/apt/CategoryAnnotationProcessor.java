package de.irf.it.tuocci.util.apt;

import de.irf.it.tuocci.annotations.Category;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: alexp
 * Date: 17.07.12
 * Time: 21:28
 * To change this template use File | Settings | File Templates.
 */
@SupportedAnnotationTypes({"de.irf.it.tuocci.annotations.Category"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CategoryAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        for (Element elem : roundEnvironment.getElementsAnnotatedWith(Category.class)) {
            Category category = elem.getAnnotation(Category.class);
            String message = "annotation found in " + elem.getSimpleName()
                    + " with complexity " + category.scheme() + category.term();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
        } // for
        return true;
    }
}
