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

package de.irf.it.tuocci.annotations.util;

import de.irf.it.tuocci.model.annotations.Action;
import de.irf.it.tuocci.model.annotations.Attribute;
import de.irf.it.tuocci.model.annotations.Category;
import de.irf.it.tuocci.model.annotations.Kind;
import de.irf.it.tuocci.model.annotations.Mixin;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
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
import java.util.Formatter;
import java.util.HashMap;
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
@SupportedAnnotationTypes("de.irf.it.tuocci.annotations.Category")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CategoryAnnotationProcessor
        extends AbstractProcessor {

    private static final String SERVICE_LOADER_FILE = "META-INF/services/de.irf.it.tuocci.model.ModelElement";

    private static final String IMPLEMENTATION_CLASS_BASE_NAME = "ModelElementImpl";

    private Map<Element, VelocityContext> contextMap;

    public CategoryAnnotationProcessor() {
        super();
        this.contextMap = new HashMap<Element, VelocityContext>();
    }

    private String generateActionModelInfoClassName(String enclosingTypeName, Category actionCategory) {
        String result;

        StringBuilder sb = new StringBuilder();
        sb.append(enclosingTypeName);

        char[] charArray = actionCategory.term().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        sb.append(charArray);
        sb.append("Action");

        result = sb.toString();

        return result;
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

        /*
         * Extract OCCI related categories.
         */
        Collection<String> occiRelated = new ArrayList<String>();

        TypeElement self = (TypeElement) element;
        TypeMirror object = eUtils.getTypeElement("java.lang.Object").asType();
        while (!tUtils.isSameType(self.asType(), object)) {
            if (!tUtils.isSameType(self.asType(), element.asType()) && self.getAnnotation(Kind.class) != null || self.getAnnotation(Mixin.class) != null) {
                occiRelated.add(self.getQualifiedName().toString());
            } // if
            self = (TypeElement) tUtils.asElement(self.getSuperclass());
        } // while

        vc.put("occiRelated", occiRelated);

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
                occiActions.add(generateActionModelInfoClassName(packageName + "." + className, e.getAnnotation(Category.class)));
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

        VelocityEngine ve = new VelocityEngine(p);
        ve.init();

        Template vt = ve.getTemplate("templates/" + IMPLEMENTATION_CLASS_BASE_NAME + ".vm");

        int i = 0;
        for (VelocityContext vc : this.contextMap.values()) {
            Messager m = processingEnv.getMessager();

            /*
             * Create qualified type name and append to result list.
             */
            String modelInfoImplClassName = vc.get("packageName") + "." + vc.get("elementName") + IMPLEMENTATION_CLASS_BASE_NAME;
            result[i++] = modelInfoImplClassName;
            /*
             * Create Java source file.
             */
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(modelInfoImplClassName);
            m.printMessage(Diagnostic.Kind.NOTE, "creating source file: " + jfo.toUri());

            Writer w = jfo.openWriter();

            m.printMessage(Diagnostic.Kind.NOTE, "applying template: " + vt.getName());
            vt.merge(vc, w);

            w.close();
        } // for

        return result;
    }

    private void writeServiceLoaderConfiguration(String[] implementationClassNames) throws IOException {
        FileObject fo = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", SERVICE_LOADER_FILE);
        PrintWriter pw = new PrintWriter(fo.openWriter());

        for(String icn : implementationClassNames) {
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
                        StringBuilder sb = new StringBuilder();
                        sb.append("unexpected annotation ");
                        sb.append("<").append(Category.class.getName()).append(">");
                        sb.append(" on ").append(element.getSimpleName());



                        String message = sb.toString();
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
                StringBuilder sb = new StringBuilder();
                sb.append("rendering of code to writer failed (message was '");
                sb.append(e.getLocalizedMessage());
                sb.append("')");

                String message = sb.toString();
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
