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

package de.irf.it.tuocci.model.annotations.processors;

import de.irf.it.tuocci.model.annotations.Action;
import de.irf.it.tuocci.model.annotations.Attaches;
import de.irf.it.tuocci.model.annotations.Attribute;
import de.irf.it.tuocci.model.annotations.Category;
import de.irf.it.tuocci.model.annotations.Kind;
import de.irf.it.tuocci.model.annotations.Mixin;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
@SupportedAnnotationTypes("de.irf.it.tuocci.model.annotations.Category")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CategoryProcessor
        extends AbstractProcessor {

    private static final String SERVICE_LOADER_FILE = "META-INF/services/de.irf.it.tuocci.model.representation.Element";

    private static final String IMPLEMENTATION_CLASS_BASE_NAME = "ElementImpl";

    private Map<Element, VelocityContext> contextMap;

    public CategoryProcessor() {
        super();
        this.contextMap = new HashMap<Element, VelocityContext>();
    }

    private String generateActionModelInfoClassName(String enclosingTypeName, Category actionCategory) {
        return String.format("%1$s%2$S%3$sAction", enclosingTypeName, actionCategory.term().charAt(0), actionCategory.term().substring(1));
    }

    private String extractOcciClass(Element element) {
        String result = null;

        Collection<Class<? extends Annotation>> classes = new ArrayList<Class<? extends Annotation>>();
        classes.add(Kind.class);
        classes.add(Mixin.class);
        classes.add(Action.class);
        for (Class<? extends Annotation> c1ass : classes) {
            Annotation a = element.getAnnotation(c1ass);
            if (a != null) {
                result = a.annotationType().getName();
                break;
            } // if
        } // while

        return result;
    }

    private Attribute[] extractOcciAttributes(Element element) {
        Collection<Attribute> result = new ArrayList<Attribute>();

        for (Element e : ElementFilter.fieldsIn(element.getEnclosedElements())) {
            Attribute a = e.getAnnotation(Attribute.class);
            if (a != null) {
                result.add(a);
            } // if
        } // for

        return result.toArray(new Attribute[result.size()]);
    }

    private void prepareTemplateContextForKindsAndMixins(Element element) {
        Elements eUtils = processingEnv.getElementUtils();
        Types tUtils = processingEnv.getTypeUtils();

        VelocityContext vc = this.contextMap.get(element);

        /*
         * Extract package name of annotated OCCI type.
         */
        String packageName = eUtils.getPackageOf(element).getQualifiedName().toString();
        vc.put("packageName", packageName);

        /*
         * Extract class name of annotated OCCI type.
         */
        String className = element.getSimpleName().toString();
        vc.put("elementName", className);

        /*
         * Extract OCCI category.
         */
        Category occiCategory = element.getAnnotation(Category.class);
        vc.put("occiCategory", occiCategory);

        /*
         * Extract OCCI class (Kind, Mixin, Action).
         */
        vc.put("occiClass", this.extractOcciClass(element));

        TypeElement self;
        TypeMirror object;
        /*
         * Extract OCCI related categories.
         */
        Collection<String> occiRelated = new ArrayList<String>();

        self = (TypeElement) element;
        object = eUtils.getTypeElement("java.lang.Object").asType();

        while (!tUtils.isSameType(self.asType(), object)) {
            if (!tUtils.isSameType(self.asType(), element.asType()) && self.getAnnotation(Kind.class) != null || self.getAnnotation(Mixin.class) != null) {
                occiRelated.add(self.getQualifiedName().toString());
            } // if
            self = (TypeElement) tUtils.asElement(self.getSuperclass());
        } // while

        vc.put("occiRelated", occiRelated);

        /*
         * Extract OCCI attachable mixins.
         */
        Collection<String> occiAttaches = new ArrayList<String>();

        self = (TypeElement) element;
        object = eUtils.getTypeElement("java.lang.Object").asType();
        String attachesName = Attaches.class.getName();

        while (!tUtils.isSameType(self.asType(), object)) {
            for (AnnotationMirror am : self.getAnnotationMirrors()) {
                if (attachesName.equals(am.getAnnotationType().toString())) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                        if ("mixins".equals(entry.getKey().getSimpleName().toString())) {
                            for (AnnotationValue av : (List<AnnotationValue>) entry.getValue().getValue()) {
                                occiAttaches.add(av.getValue().toString());
                            } // for
                        } // if
                    } // for
                } // if
            } // for
            self = (TypeElement) tUtils.asElement(self.getSuperclass());
        } // while

        vc.put("occiAttaches", occiAttaches);

        /*
         * Extract OCCI attributes.
         */
        vc.put("occiAttributes", this.extractOcciAttributes(element));

        /*
         * Extract OCCI actions.
         */
        Collection<String> occiActions = new ArrayList<String>();

        for (Element e : ElementFilter.methodsIn(element.getEnclosedElements())) {
            Action a = e.getAnnotation(Action.class);
            if (a != null) {
                String qualifiedClassName = String.format("%1$s.%2$s", packageName, className);
                occiActions.add(generateActionModelInfoClassName(qualifiedClassName, e.getAnnotation(Category.class)));
            } // if
        } // for

        vc.put("occiActions", occiActions);
    }

    private void prepareTemplateContextForActions(Element element) {
        Elements eUtils = processingEnv.getElementUtils();

        VelocityContext vc = new VelocityContext();

        /*
         * Extract OCCI category.
         */
        Category occiCategory = element.getAnnotation(Category.class);
        vc.put("occiCategory", occiCategory);

        /*
         * Extract package name of annotated OCCI type.
         */
        String packageName = eUtils.getPackageOf(element).getQualifiedName().toString();
        vc.put("packageName", packageName);

        /*
         * Extract class name of annotated OCCI type.
         */
        String actionName = this.generateActionModelInfoClassName(element.getEnclosingElement().getSimpleName().toString(), occiCategory);
        vc.put("elementName", actionName);

        /*
         * Extract OCCI class (Kind, Mixin, Action).
         */
        vc.put("occiClass", this.extractOcciClass(element));

        /*
         * Extract OCCI attributes.
         */
        vc.put("occiAttributes", this.extractOcciAttributes(element));

        /*
         * Add to element list.
         */
        this.contextMap.put(element, vc);
    }

    private String[] renderModelInfoClasses()
            throws IOException {
        String[] result = new String[this.contextMap.size()];

        Properties p = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        p.load(url.openStream());

        VelocityEngine engine = new VelocityEngine(p);
        engine.init();

        String templatePath = String.format("templates/%1$s.vm", IMPLEMENTATION_CLASS_BASE_NAME);
        Template vt = engine.getTemplate(templatePath);

        int i = 0;
        for (VelocityContext vc : this.contextMap.values()) {
            vc.put("baseName", IMPLEMENTATION_CLASS_BASE_NAME);

            /*
             * Create qualified type name and append to result list.
             */
            String modelInfoImplClassName = String.format("%1$s.%2$s%3$s", vc.get("packageName"), vc.get("elementName"), IMPLEMENTATION_CLASS_BASE_NAME);
            result[i++] = modelInfoImplClassName;
            /*
             * Create Java source file.
             */
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(modelInfoImplClassName);
            Writer w = jfo.openWriter();
            vt.merge(vc, w);

            w.close();
        } // for

        return result;
    }

    private void writeServiceLoaderConfiguration(String[] implementationClassNames)
            throws IOException {
        FileObject fo = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", SERVICE_LOADER_FILE);
        PrintWriter pw = new PrintWriter(fo.openWriter());

        for (String icn : implementationClassNames) {
            pw.println(icn);
        } // for

        pw.flush();
        pw.close();
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        boolean result;

        /*
         * Check compiler pass, and only act on first round.
         */
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Category.class);
        if (!roundEnvironment.processingOver() && !elements.isEmpty()) {
            /*
             * Store all OCCI resource types.
             */
            for (Element rootElement : roundEnvironment.getRootElements()) {
                if (elements.contains(rootElement)) {
                    this.contextMap.put(rootElement, new VelocityContext());
                } // if
            } // for

            /*
             * Iterate over all elements with a @Category annotation.
             */
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Category.class)) {
                switch (element.getKind()) {
                    case CLASS:
                        this.prepareTemplateContextForKindsAndMixins(element);
                        break;
                    case METHOD:
                        this.prepareTemplateContextForActions(element);
                        break;
                    default:
                        String message = String.format("unexpected annotation <%1$s> on %2$s", Category.class.getName(), element.getSimpleName());
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
                        throw new RuntimeException(message);
                } // switch
            } // for
            result = true;
        } // if
        else {
            try {
                String[] implementationClassNames = this.renderModelInfoClasses();
                this.writeServiceLoaderConfiguration(implementationClassNames);
            } // try
            catch (IOException e) {
                String message = String.format("rendering of code to writer failed (message was '%1$s')", e.getLocalizedMessage());
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
                throw new RuntimeException(message, e);
            } // catch
            finally {
                result = false;
            } // finally
        } // if

        return result;
    }
}
