/*
 * This file is part of tuOCCI.
 *
 *     tuOCCI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     tuOCCI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with tuOCCI.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.irf.it.tuocci.util.apt;

import de.irf.it.tuocci.annotations.Category;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.Set;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
@SupportedAnnotationTypes("de.irf.it.tuocci.annotations.Category")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CategoryAnnotationProcessor
        extends AbstractProcessor {

    public static final String DETECTED_PROPERTIES = "de.irf.it.tuocci.category.detected.properties";

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        boolean result;

        /*
         * Check compiler pass, and only act on first round.
         */
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Category.class);
        if (!roundEnvironment.processingOver() && !elements.isEmpty()) {
            for(TypeElement te : typeElements) {
                System.out.println(te.getQualifiedName());
            }
            for(Element e: roundEnvironment.getRootElements()) {
                System.out.println(e.getSimpleName());
            }
            /*
             * Collect elements in property set.
             */
            Properties p = new Properties();
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Category.class)) {
                /*
                 * Check if element is a type (could be method for OCCI actions).
                 */
                if (ElementKind.CLASS.equals(element.getKind())) {
                    Category c = element.getAnnotation(Category.class);
                    String category = c.scheme() + c.term();
                    String type = ((TypeElement) element).getQualifiedName().toString();
                    p.setProperty(category, type);
                } // if
            } // for

            /*
             * Write out property set to disk.
             */
            try {
                FileObject fo = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", DETECTED_PROPERTIES);
                Writer w = fo.openWriter();
                p.store(w, "tuOCCI: Result of Kind/Mixin detection on classpath");
                w.flush();
                w.close();
            } // try
            catch (IOException e) {
                StringBuffer sb = new StringBuffer();
                sb.append("failed to store <");
                sb.append(StandardLocation.SOURCE_OUTPUT).append(File.separator).append(DETECTED_PROPERTIES);
                sb.append("> as property set (message was '");
                sb.append(e.getLocalizedMessage());
                sb.append("')");

                String message = sb.toString();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
                throw new RuntimeException(message, e);
            } // catch
            finally {
                result = true;
            } // finally
        } // if
        else {
            // do nothing here.
            result = false;
        } // if

        return result;
    }
}
