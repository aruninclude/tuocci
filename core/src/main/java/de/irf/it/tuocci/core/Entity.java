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

package de.irf.it.tuocci.core;

import de.irf.it.tuocci.model.Queryable;
import de.irf.it.tuocci.model.Registry;
import de.irf.it.tuocci.model.annotations.Action;
import de.irf.it.tuocci.model.annotations.Attaches;
import de.irf.it.tuocci.model.annotations.Attribute;
import de.irf.it.tuocci.model.annotations.Category;
import de.irf.it.tuocci.model.annotations.Kind;
import de.irf.it.tuocci.model.annotations.Mixin;
import de.irf.it.tuocci.model.exceptions.ActionTriggerException;
import de.irf.it.tuocci.model.exceptions.AttributeAccessException;
import de.irf.it.tuocci.model.exceptions.InvalidMixinException;
import de.irf.it.tuocci.model.exceptions.UnsupportedMixinException;
import de.irf.it.tuocci.model.representation.Element;
import de.irf.it.tuocci.model.representation.Tag;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The root type for all resources as described in the OCCI Core Model.
 * <p/>
 * The <code>Entity</code> class represents the root type in the OCCI Core
 * Model type system. All OCCI-compliant resources are derived from this class.
 * It exposes mechanisms for managing an OCCI resource with respect to its
 * fundamental attributes, ({@link #id} and {@link #title}, and provides
 * facilities for managing a resource's mixins.
 * <p/>
 * This <code>Entity</code> exposes methods for the generic management of more
 * specific OCCI resource sub-types, including
 * <ul>
 * <li>attachment and detachment of mixins</li>
 * <li>retrieval and manipulation of attributes, and</li>
 * <li>invocation of actions.</li>
 * </ul>
 * In addition, it ensures the correct attribution in the OCCI term/scheme
 * model.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de">Alexander
 *         Papaspyrou</a>
 * @version $Id$
 * @see "Ralf Nyrén, Andy Edmonds, Alexander Papaspyrou, and Thijs Metsch, <a
 *      href="http://ogf.org/documents/GFD.183.pdf">Open Cloud Computing
 *      Interface &ndash; Core</a>, Open Grid Forum Proposed Recommendation,
 *      GFD-P-R.183, April 2011, Section 4.5.1"
 * @since 0.3 ("gordons")
 */
@Category(term = "entity", scheme = "http://schemas.ogf.org/occi/core#", title = "Entity type")
@Kind
@Attaches(mixins = {Tag.class})
public abstract class Entity
        implements Queryable {

    /**
     * A unique identifier (within the service provider's namespace) of this
     * Entity.
     */
    @Attribute(name = "occi.core.id")
    private URI id;

    /**
     * The display name of this entity.
     */
    @Attribute(name = "occi.core.title", required = false, mutable = true)
    private String title;

    /**
     * Creates a new instance of this class.
     * <p/>
     * During creation, a random identifier for this entity is generated using
     * an <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC
     * 4122-compliant UUID</a> in the corresponding URN namespace
     * (<code>urn:uuid:</code><i>uuid</i>).
     */
    protected Entity() {
        this(URI.create(new StringBuffer("urn:uuid:").append(UUID.randomUUID().toString()).toString()));
    }

    /**
     * Creates a new instance of this class, using the given parameters.
     *
     * @param id
     *         The unique identifier to be used for this class. It is
     *         <b>recommended</b> that an
     *         <a href="http://www.ietf.org/rfc/rfc4122.txt" RFC 4122-compliant
     *         UUID</a> is used and the appropriate namespace ("urn:uuid") is
     *         exposed.
     */
    protected Entity(URI id) {
        this.id = id;
        this.mixins = new HashSet<Queryable>();
    }

    /**
     * Returns the unique identifier of this entity.
     *
     * @return The unique identifier of this entity.
     */
    public URI getId() {
        return id;
    }

    /**
     * Returns the title of this entity.
     *
     * @return The title of this entity.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this entity.
     *
     * @param title
     *         The new title of this entity.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    // -------------------------------------------------------------------------
    //  Handling of mixins
    // -------------------------------------------------------------------------

    /**
     * A set of mixins currently attached to this entity.
     *
     * @see #attachMixin(Queryable)
     */
    private Set<Queryable> mixins;

    /**
     * Provides a set of currently registered mixins. A copy which is
     * unmodifiable is returned.
     *
     * @return An unmodifiable set of currently registered mixins.
     */
    public Queryable[] getMixins() {
        return this.mixins.toArray(new Queryable[this.mixins.size()]);
    }

    /**
     * Attaches an object as mixin to this entity.
     * <p/>
     * More specifically, a given, {@link Mixin}-annotated object is registered
     * with this entity to be available with its attributes and actions.
     * <p/>
     * During attachment, several semantic checks are done:
     * <ol>
     * <li>the object carries a {@link Mixin}</li> annotation,</li>
     * <li>the object carries a {@link Category} annotation, and</li>
     * <li>
     * this entity supports the provided mixin as indicated by its
     * {@link Attaches} annotation.
     * </li>
     * </ol>
     * If either of the former are missing, or the latter is indicated, an
     * exception is thrown.
     *
     * @param mixin
     *         The object to be attached as a mixin to this entity.
     * @throws InvalidMixinException
     * @throws UnsupportedMixinException
     *         if the provided object is missing either {@link Mixin} or {@link
     *         Category} annotations, or the provided object's class is not
     *         listed in this entity's {@link Attaches} annotation.
     */
    public final void attachMixin(Queryable mixin)
            throws UnsupportedMixinException {
        Element self = Registry.getInstance().findElementForClass(this.getClass());

        /*
        * Check case to handle.
        */
        Element mixinElement = Registry.getInstance().findElementForClass(mixin.getClass());
        if (mixin instanceof Tag) {
            /*
             * found Tag: Attach without further checks.
             */
            this.mixins.add(mixin);
        } // if
        else if (mixinElement.getType().equals(Element.Type.MIXIN)) {
            /*
             * found Mixin: Check whether the provided mixin is attachable to
             * this kind and add, if appropriate.
             */
            boolean attached = false;
            for (Element attachable : self.getAttaches()) {
                if (mixinElement.equals(attachable)) {
                    attached = this.mixins.add(mixin);
                    if (attached) {
                        break;
                    } // if
                } // if
                else {
                    String message = String.format("not supported: '%1$s' cnanot attach mixins of type '%2$s'", self, mixinElement);
                    throw new UnsupportedMixinException(message);
                } // else
            } // for
        } // else if
        else {
            /*
             * found something else: raise exception.
             */
            String message = String.format("not a mixin: '%1$s' cannot be attached", mixin);
            throw new UnsupportedMixinException(message);
        } // else
    }

    /**
     * Detaches the given object from the list of currently registered mixins.
     * <p/>
     * After removal, the mixin represented by the removed object cannot be
     * addressed anymore from this entity.
     *
     * @param o
     *         The mixin object to be removed from this entity.
     */
    public final void detachMixin(Object o) {
        this.mixins.remove(o);
    }

    // -------------------------------------------------------------------------
    //  Implementation of de.irf.it.tuocci.model.Queryable
    // -------------------------------------------------------------------------

    @Override
    public de.irf.it.tuocci.model.representation.Attribute[] retrieveAccessibleAttributes() {
        Set<de.irf.it.tuocci.model.representation.Attribute> result = new HashSet<de.irf.it.tuocci.model.representation.Attribute>();

        Class<?> self = this.getClass();
        /*
         * Search on this entity, climbing up the related hierarchy.
         */
        while (!Object.class.equals(self)) {
            Element e = Registry.getInstance().findElementForClass(self);
            Collections.addAll(result, e.getAttributes());
        } // while

        /*
         * Search on all mixins.
         */
        for (Queryable mixin : this.getMixins()) {
            Collections.addAll(result, mixin.retrieveAccessibleAttributes());
        } // for

        return result.toArray(new de.irf.it.tuocci.model.representation.Attribute[result.size()]);
    }

    /**
     * Retrieves the field for an attribute with the provided name for the
     * given
     * object.
     *
     * @param object
     *         The object to be inspected.
     * @param name
     *         The name of the attribute the corresponding field is to be
     *         retrieved.
     * @return The field corresponding to the provided attribute name, if
     *         available; <code>null</code>, otherwise.
     */
    private Field findFieldForAttribute(Object object, String name) {
        Field result = null;

        Class<?> self = object.getClass();

        /*
        * Search on this entity, climbing up the class hierarchy.
        */
        while (!Object.class.equals(self)) {
            for (Field f : this.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(Attribute.class)
                        && f.getAnnotation(Attribute.class).name().equals(name)) {
                    result = f;
                    break;
                } // if
            } // for
            self = self.getSuperclass();
        } // while

        return result;
    }

    /**
     * Retrieves the getter/setter for a given field using reflection and the
     * JavaBeans Introspection API. If no corresponding method exists, nothing
     * will be returned.
     *
     * @param field
     *         The field for which getter/setter are to be retrieved
     * @param flag
     *         An indicator to which method should be retrieved;
     *         <code>true</code> returns the getter, while <code>false</code>
     *         returns the setter.
     * @return The getter/setter for the provided field, if available; or
     *         <code>null</code>, otherwise.
     */
    private Method retrieveAttributeGetterSetter(Field field, boolean flag) {
        Method result = null;

        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(this.getClass());
        } // try
        catch (IntrospectionException e) {
            e.printStackTrace();  // TODO: not implemented yet.
        } // catch

        assert bi != null;
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals(field.getName())) {
                result = (flag ? pd.getReadMethod() : pd.getWriteMethod());
                break;
            } // if
        } // for

        return result;
    }

    @Override
    public final String getValue(de.irf.it.tuocci.model.representation.Attribute attribute)
            throws AttributeAccessException {
        String result;

        Method getter;
        Object target = null;

        /*
         * Search for field corresponding to provided attribute name.
         */
        Field f = this.findFieldForAttribute(this, attribute.getName());
        if (f != null) {
            /*
             * Found on entity.
             */
            target = this;
        } // if
        else {
            for (Queryable mixin : this.mixins) {
                f = this.findFieldForAttribute(mixin, attribute.getName());
                if (f != null) {
                    /*
                     * Found on mixin.
                     */
                    target = mixin;
                } // if
            } // for
        } // else

        /*
         * Retrieve value.
         */
        if (f != null) {
            getter = this.retrieveAttributeGetterSetter(f, true);
            Object o;
            try {
                o = getter.invoke(target);
            } // try
            catch (IllegalAccessException e) {
                String message = String.format("retrieval of '%1$s' attribute on %2$s failed: corresponding getter not available or not public", attribute.getName(), target);
                throw new AttributeAccessException(message, e);
            } // catch
            catch (InvocationTargetException e) {
                String message = String.format("retrieval of '%1$s' attribute on %2$s failed: underlying getter raised an exception (message was '%3$s')", attribute.getName(), target, e.getLocalizedMessage());
                throw new AttributeAccessException(message, e);
            } // catch
            result = o.toString();
        } // if
        else {
            String message = String.format("retrieval of '%1$s' attribute on %2$s failed: no corresponding field found", attribute.getName(), target);
            throw new AttributeAccessException(message);
        } // else

        return result;
    }

    @Override
    public final void setValue(de.irf.it.tuocci.model.representation.Attribute attribute, String value)
            throws AttributeAccessException {
        Method setter = null;
        Object target = null;

        /*
         * Search for field corresponding to provided attribute name.
         */
        Field f = this.findFieldForAttribute(this, attribute.getName());
        if (f != null) {
            /*
             * Found on entity.
             */
            target = this;
        } // if
        else {
            for (Queryable mixin : this.mixins) {
                f = this.findFieldForAttribute(mixin, attribute.getName());
                if (f != null) {
                    /*
                     * Found on mixin.
                     */
                    target = mixin;
                } // if
            } // for
        } // else

        /*
         * Manipulate value.
         */
        if (f != null) {
            setter = this.retrieveAttributeGetterSetter
                    (f, false);

            Object o = this.instanceFromString(f.getDeclaringClass(), value);
            if (o == null) {
                String message = String.format("modification of '%1$s' attribute on %2$s failed: no single argument constructor, 'fromString', or 'valueOf' method for conversion to %3$s  found", attribute.getName(), target, f.getDeclaringClass());
                throw new AttributeAccessException(message);
            } //

            try {
                setter.invoke(target, o);
            }  // try
            catch (IllegalAccessException e) {
                String message = String.format("modification of '%1$s' attribute on %2$s failed: corresponding setter not available or not public", attribute.getName(), target);
                throw new AttributeAccessException(message, e);
            } // catch
            catch (InvocationTargetException e) {
                String message = String.format("modification of '%1$s' attribute on %2$s failed: underlying setter raised an exception (message was '%3$s')", attribute.getName(), target, e.getLocalizedMessage());
                throw new AttributeAccessException(message, e);
            } // catch
        } // if
        else {
            String message = String.format("manipulation of '%1$s' attribute on %2$s failed: no corresponding field found", attribute.getName(), target);
            throw new AttributeAccessException(message);
        } // else
    }

    /**
     * Creates a new instance of the requested class via its single {@link
     * String} argument constructor.
     *
     * @param type
     *         The class of the instance to be created.
     * @param value
     *         The value for the constructor argument.
     * @return A new instance of the requested class, properly initialized with
     *         the given value, if successful; <code>null</code>, otherwise.
     */
    private Object instanceViaConstructor(Class<?> type, String value) {
        Object result = null;

        Constructor<?> c = null;
        try {
            c = type.getConstructor(String.class);
        } // try
        catch (NoSuchMethodException e) {
            // This space intentionally left blank.
        } // catch

        if (c != null) {
            try {
                result = c.newInstance(value);
            } // try
            catch (InstantiationException e) {
                // This space intentionally left blank.
            } // catch
            catch (IllegalAccessException e) {
                // This space intentionally left blank.
            } // catch
            catch (InvocationTargetException e) {
                // This space intentionally left blank.
            } // catch
        } // if

        return result;
    }

    /**
     * Creates a new instance of the requested class via a single argument
     * method.
     *
     * @param type
     *         The class of the instance to be created.
     * @param methodName
     *         The name of the single {@link String} argument method to be
     *         invoked.
     * @param value
     *         The value for the constructor argument.
     * @return A new instance of the requested class, properly initialized with
     *         the given value, if successful; <code>null</code>, otherwise.
     */
    private Object instanceViaMethod(Class<?> type, String methodName, String value) {
        Object result = null;

        Method m = null;
        try {
            m = type.getMethod(methodName, String.class);
        } // try
        catch (NoSuchMethodException e) {
            // This space intentionally left blank.
        }  // catch

        if (m != null) {
            try {
                result = m.invoke(null, value);
            } // try
            catch (IllegalAccessException e) {
                // This space intentionally left blank.
            } // catch
            catch (InvocationTargetException e) {
                // This space intentionally left blank.
            } // catch
        } // if

        return result;
    }

    /**
     * Creates a new instance of the requested class using the given argument.
     * <p/>
     * In that order, the following approaches are used to achieve this:
     * <ol>
     * <li>A single {@link String} argument constructor,</li>
     * <li>A <code>fromString(String)</code> method, and</li>
     * <lI>A <code>valueOf(String)</code> method.</lI>
     * </ol>
     *
     * @param c
     *         The class of the instance to be created.
     * @param value
     *         The value for the argument.
     * @return A new instance of the requested class, properly initialized with
     *         the given value, if successful; <code>null</code>, otherwise.
     */
    private Object instanceFromString(Class<?> c, String value) {
        Object result;

        result = this.instanceViaConstructor(c, value);
        if (result == null) {
            result = this.instanceViaMethod(c, "fromString", value);
        } // if
        if (result == null) {
            result = this.instanceViaMethod(c, "valueOf", value);
        } // if

        return result;
    }

    /**
     * Retrieves the method representing an action as indentified by the
     * provided term/scheme combination, using reflection and the JavaBeans
     * Introspection API. If no corresponding method exists, nothing will be
     * returned.
     *
     * @param object
     *         The object for which the method is to be retrieved
     * @param term
     *         The OCCI term part of the action for which the method is to be
     *         retrieved.
     * @param scheme
     *         The OCCI scheme part of the action for which the method is to be
     *         retrieved.
     * @return The method representing the action as identified by the provided
     *         term/scheme combination, if available; or <code>null</code>,
     *         otherwise.
     */
    private Method findMethodForAction(Object object, String term, String scheme) {
        Method result = null;

        Class<?> self = object.getClass();

        /*
        * Search on this entity, climbing up the class hierarchy.
        */
        while (!Object.class.equals(self)) {
            for (Method m : self.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Action.class)
                        && m.isAnnotationPresent(Category.class)
                        && m.getAnnotation(Category.class).term().equals(term)
                        && m.getAnnotation(Category.class).scheme().equals
                        (scheme)) {
                    result = m;
                } // if
            } // if
            self = self.getSuperclass();
        } // while

        return result;
    }

    @Override
    public final void triggerAction(Element action, Map<de.irf.it.tuocci.model.representation.Attribute, String> attributes)
            throws ActionTriggerException {
        Method method = null;
        Object target = null;

        /*
         * Search for method corresponding to provided term/scheme values.
         */
        method = this.findMethodForAction(this, action.getCategory().getTerm(), action.getCategory().getScheme().toString());
        if (method != null) {
            /*
             * Found on entity.
             */
            target = this;
        } // if
        else {
            for (Object o : this.mixins) {
                method = this.findMethodForAction(o, action.getCategory().getTerm(), action.getCategory().getScheme().toString());
                if (method != null) {
                    /*
                     * Found on mixin.
                     */
                    target = o;
                } // if
            } // for
        } // else

        /*
        * Trigger action.
        */
        if (method != null) {
            Class<?>[] pts = method.getParameterTypes();
            List<Object> invocationArguments = new ArrayList<Object>();

            /*
             * Gather attributes for action triggering.
             */
            if (pts.length == attributes.size()) {
                for (Class<?> pt : pts) {
                    Attribute a = pt.getAnnotation(Attribute.class);
                    if (attributes.containsKey(a.name())) {
                        Object o = this.instanceFromString(pt, attributes.get(a.name()));
                        if (o == null) {
                            String message = new StringBuffer("invocation of '")
                                    .append(action.getCategory().getScheme().toString()).append(action.getCategory().getTerm())
                                    .append("' with attribute '")
                                    .append(a.name())
                                    .append("' failed: no single argument constructor, 'fromString', or 'valueOf' method for conversion to ")
                                    .append(pt)
                                    .append(" found")
                                    .toString();
                            throw new ActionTriggerException(message);
                        } // if
                        else {
                            invocationArguments.add(o);
                        } // else
                    } // if
                    else {
                        String message = new StringBuffer("triggering of action '")
                                .append(action.getCategory().getScheme().toString()).append(action.getCategory().getTerm())
                                .append("' failed: provided attributes do not match action signature")
                                .toString();
                        throw new ActionTriggerException(message);
                    } // else

                } // for

                /*
                 * Invoke corresponding method with arguments.
                 */
                try {
                    method.invoke(target, invocationArguments.toArray());
                } // try
                catch (IllegalAccessException e) {
                    String message = new StringBuffer("triggering of action '")
                            .append(action.getCategory().getScheme().toString()).append(action.getCategory().getTerm())
                            .append("' failed: method static, not public, or otherwise inaccessible")
                            .toString();
                    throw new ActionTriggerException(message, e);
                } // catch
                catch (InvocationTargetException e) {
                    String message = new StringBuffer("modification of action '")
                            .append(action.getCategory().getScheme().toString()).append(action.getCategory().getTerm())
                            .append("' failed: underlying method raised an exception (")
                            .append(e.getMessage())
                            .append(")")
                            .toString();
                    throw new ActionTriggerException(message, e);
                } // catch

            } // if
            else {
                String message = new StringBuffer("triggering of action '")
                        .append(action.getCategory().getScheme().toString()).append(action.getCategory().getTerm())
                        .append("' failed: number of attributes (providing ")
                        .append(attributes.size())
                        .append(") does not match action signature (requiring ")
                        .append(pts.length)
                        .append(")")
                        .toString();
                throw new ActionTriggerException(message);
            } // else
        } // if
        else {
            String message = new StringBuffer("triggering of action '")
                    .append(action.getCategory().getScheme().toString()).append(action.getCategory().getTerm())
                    .append("' failed: no corresponding method found on ")
                    .append(this.toString())
                    .toString();
            throw new ActionTriggerException(message);
        } // else
    }

}
