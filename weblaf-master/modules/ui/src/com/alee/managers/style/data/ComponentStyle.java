/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.managers.style.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.api.clone.behavior.PreserveOnClone;
import com.alee.api.jdk.Objects;
import com.alee.api.merge.Merge;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.managers.style.ComponentDescriptor;
import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CollectionUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.InsetsUIResource;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Component style information class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
@XStreamAlias ( "style" )
@XStreamConverter ( ComponentStyleConverter.class )
public final class ComponentStyle implements CloneBehavior<ComponentStyle>, Serializable
{
    /**
     * Style component type.
     * Refers to identifier of a component this style belongs to.
     *
     * @see ComponentDescriptor#getId()
     */
    @XStreamAsAttribute
    private String type;

    /**
     * Unique component style identifier.
     * Default component style depends on component type and might also depend on the instance.
     *
     * @see ComponentDescriptor#getDefaultStyleId()
     * @see ComponentDescriptor#getDefaultStyleId(JComponent)
     * @see StyleId#getDefault(JComponent)
     * @see StyleId#getDefault(Window)
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Different style identifier which is extended by this style.
     * Any existing style identifier of the same component type can be used here, though extended style must be defined first.
     */
    @XStreamAsAttribute
    private String extendsId;

    /**
     * Component's settings.
     * Contains field-value pairs which will be applied to component fields.
     */
    private LinkedHashMap<String, Object> componentProperties;

    /**
     * Component's UI settings.
     * Contains field-value pairs which will be applied to component UI fields.
     */
    private LinkedHashMap<String, Object> uiProperties;

    /**
     * {@link PainterStyle} for component's {@link Painter}.
     *
     * @see PainterStyle
     */
    private PainterStyle painterStyle;

    /**
     * {@link List} of nested {@link ComponentStyle}s.
     */
    private List<ComponentStyle> nestedStyles;

    /**
     * Parent {@link ComponentStyle}.
     * This variable is only set in runtime for style usage convenience.
     */
    @PreserveOnClone
    private transient ComponentStyle parent;

    /**
     * Returns supported component type.
     *
     * @return supported component type
     */
    @NotNull
    public String getType ()
    {
        return type;
    }

    /**
     * Sets supported component type.
     *
     * @param type new supported component type
     */
    public void setType ( @NotNull final String type )
    {
        this.type = type;
    }

    /**
     * Returns component style identifier.
     *
     * @return component style identifier
     */
    @NotNull
    public String getId ()
    {
        return id;
    }

    /**
     * Sets component {@link StyleId}.
     *
     * @param id new component {@link StyleId}
     */
    public void setId ( @NotNull final String id )
    {
        this.id = id;
    }

    /**
     * Returns complete style identifier.
     * Not that it will also include types of each of the parents.
     *
     * @return complete style identifier
     * @see StyleId#getCompleteId()
     */
    @NotNull
    public String getCompleteId ()
    {
        final ComponentStyle parentStyle = getParent ();
        return parentStyle != null ? parentStyle.getPathId () + StyleId.styleSeparator + getId () : getId ();
    }

    /**
     * Returns path for complete {@link StyleId}.
     * Not that it will also include types of each of the parents.
     * Similar to {@code StyleId#getPathId(JComponent)} method.
     *
     * @return path for complete {@link StyleId}
     */
    @NotNull
    private String getPathId ()
    {
        // Full identifier for this part of the path
        final String fullId = getType () + ":" + getId ();

        // Combined identifiers path
        final ComponentStyle parentStyle = getParent ();
        return parentStyle != null ? parentStyle.getPathId () + StyleId.styleSeparator + fullId : fullId;
    }

    /**
     * Returns extended component {@link StyleId} or null if none extended.
     *
     * @return extended component {@link StyleId} or null if none extended
     */
    @Nullable
    public String getExtendsId ()
    {
        return extendsId;
    }

    /**
     * Sets extended component {@link StyleId}.
     * Set this to null in case you don't want to extend any style.
     *
     * @param id new extended component {@link StyleId}
     */
    public void setExtendsId ( @Nullable final String id )
    {
        this.extendsId = id;
    }

    /**
     * Returns component properties.
     *
     * @return component properties
     */
    @NotNull
    public LinkedHashMap<String, Object> getComponentProperties ()
    {
        return componentProperties;
    }

    /**
     * Sets component properties.
     *
     * @param componentProperties new component properties
     */
    public void setComponentProperties ( @NotNull final LinkedHashMap<String, Object> componentProperties )
    {
        this.componentProperties = componentProperties;
    }

    /**
     * Returns component UI properties.
     *
     * @return component UI properties
     */
    @NotNull
    public LinkedHashMap<String, Object> getUIProperties ()
    {
        return uiProperties;
    }

    /**
     * Sets component UI properties
     *
     * @param uiProperties new component UI properties
     */
    public void setUIProperties ( @NotNull final LinkedHashMap<String, Object> uiProperties )
    {
        this.uiProperties = uiProperties;
    }

    /**
     * Returns component's {@link PainterStyle}.
     *
     * @return component's {@link PainterStyle}
     */
    @Nullable
    public PainterStyle getPainterStyle ()
    {
        return painterStyle;
    }

    /**
     * Sets component's {@link PainterStyle}.
     *
     * @param painterStyle new component's {@link PainterStyle}
     */
    public void setPainterStyle ( @Nullable final PainterStyle painterStyle )
    {
        this.painterStyle = painterStyle;
    }

    /**
     * Returns {@link List} of nested {@link ComponentStyle}s.
     *
     * @return {@link List} of nested {@link ComponentStyle}s
     */
    @NotNull
    public List<ComponentStyle> getNestedStyles ()
    {
        return nestedStyles;
    }

    /**
     * Sets {@link List} of nested {@link ComponentStyle}s.
     *
     * @param nestedStyles new {@link List} of nested {@link ComponentStyle}s
     */
    public void setNestedStyles ( @NotNull final List<ComponentStyle> nestedStyles )
    {
        this.nestedStyles = nestedStyles;
    }

    /**
     * Returns nested styles count.
     *
     * @return nested styles count
     */
    public int getStylesCount ()
    {
        return getNestedStyles ().size ();
    }

    /**
     * Returns parent {@link ComponentStyle}.
     *
     * @return parent {@link ComponentStyle}
     */
    @Nullable
    public ComponentStyle getParent ()
    {
        return parent;
    }

    /**
     * Sets parent {@link ComponentStyle}.
     *
     * @param parent parent {@link ComponentStyle}
     */
    public void setParent ( @Nullable final ComponentStyle parent )
    {
        this.parent = parent;
    }

    /**
     * Applies this {@link ComponentStyle} to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to apply this {@link ComponentStyle} to
     */
    public void apply ( @NotNull final JComponent component )
    {
        try
        {
            final ComponentUI ui = getComponentUI ( component );

            // Applying component properties
            applyProperties ( component, appendEmptyComponentProperties ( getComponentProperties () ) );

            // Applying UI properties
            applyProperties ( ui, getUIProperties () );

            // Installing painter
            // todo Only reapply settings if painter already exists?
            // todo Update component instead of reinstalling painter if it is the same?
            final Painter customPainter = StyleManager.getCustomPainter ( component );
            final Painter painter = customPainter != null ? customPainter : createPainter ( getPainterStyle () );
            PainterSupport.setPainter ( component, ui, painter );
        }
        catch ( final Exception e )
        {
            throw new StyleException ( String.format ( "Unable to apply style '%s' to component: %s", getCompleteId (), component ), e );
        }
    }

    /**
     * Returns new {@link Painter} instance for the specified {@link PainterStyle}.
     *
     * @param painterStyle {@link PainterStyle}
     * @return new {@link Painter} instance for the specified {@link PainterStyle}
     * @throws NoSuchFieldException      when some painter settings could not be applied
     * @throws NoSuchMethodException     when some painter settings could not be applied
     * @throws InvocationTargetException when some painter settings could not be applied
     * @throws IllegalAccessException    when some painter settings could not be applied
     */
    @NotNull
    private Painter createPainter ( @NotNull final PainterStyle painterStyle )
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException
    {
        // Creating painter instance
        // Be aware that all painters must have default constructor
        final String painterClass = painterStyle.getPainterClass ();
        final Painter painter = ReflectUtils.createInstanceSafely ( painterClass );
        if ( painter == null )
        {
            final String msg = "Unable to create painter '%s' in style '%s'";
            throw new StyleException ( String.format ( msg, painterClass, getId () ) );
        }

        // Applying painter properties
        // These properties are applied only for style-provided painters
        applyProperties ( painter, painterStyle.getProperties () );

        return painter;
    }

    /**
     * Applies properties to specified object fields.
     *
     * @param object         object instance
     * @param skinProperties skin properties to apply, these properties come from the skin
     * @throws NoSuchFieldException      if painter could not be set into object
     * @throws NoSuchMethodException     if painter setter method could not be found
     * @throws InvocationTargetException if painter setter method invocation failed
     * @throws IllegalAccessException    if painter setter method is not accessible
     */
    private void applyProperties ( @NotNull final Object object, @Nullable final Map<String, Object> skinProperties )
            throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        // Applying merged properties
        if ( skinProperties != null && skinProperties.size () > 0 )
        {
            for ( final Map.Entry<String, Object> entry : skinProperties.entrySet () )
            {
                final Object value = entry.getValue ();
                if ( value instanceof PainterStyle )
                {
                    // PainterStyle is handled differently
                    final Painter painter = createPainter ( ( PainterStyle ) value );
                    setFieldValue ( object, entry.getKey (), painter );
                }
                else if ( entry.getKey ().equals ( "layout" ) && object instanceof Container )
                {
                    // Ensure we do not replace LayoutManager provided by user
                    if ( value == null || value instanceof LayoutManager )
                    {
                        final Container container = ( Container ) object;
                        final LayoutManager oldLayout = container.getLayout ();
                        if ( isReplaceableLayout ( container, oldLayout ) || !( value instanceof UIResource ) )
                        {
                            // Creating clone of new layout manager
                            // This is important to avoid same layout being set everywhere
                            final LayoutManager newLayout = Clone.deep ().clone ( ( LayoutManager ) value );

                            // Migrating layout manager settings if possible
                            if ( newLayout != null && newLayout instanceof AbstractLayoutManager )
                            {
                                ( ( AbstractLayoutManager ) newLayout ).migrate ( container, oldLayout );
                            }

                            // Updating layout manager
                            container.setLayout ( newLayout );
                        }
                    }
                    else
                    {
                        throw new StyleException ( "Value provided for Container `layout` property is not LayoutManager" );
                    }
                }
                else if ( entry.getKey ().equalsIgnoreCase ( ComponentStyleConverter.MARGIN_ATTRIBUTE ) && object instanceof JComponent )
                {
                    // Temporary workaround for margin attribute
                    PainterSupport.setMargin ( ( JComponent ) object, ( Insets ) entry.getValue () );
                }
                else if ( entry.getKey ().equalsIgnoreCase ( ComponentStyleConverter.PADDING_ATTRIBUTE ) && object instanceof JComponent )
                {
                    // Temporary workaround for padding attribute
                    PainterSupport.setPadding ( ( JComponent ) object, ( Insets ) entry.getValue () );
                }
                else
                {
                    // Other fields are simply set through common means
                    setFieldValue ( object, entry.getKey (), value );
                }
            }
        }
    }

    /**
     * Returns whether or not {@link LayoutManager} of the specified {@link Container} is replaceable.
     *
     * @param container {@link Container}
     * @param oldLayout {@link LayoutManager}
     * @return {@code true} if  {@link LayoutManager} of the specified {@link Container} is replaceable, {@code false} otherwise
     */
    private boolean isReplaceableLayout ( @NotNull final Container container, @Nullable final LayoutManager oldLayout )
    {
        return oldLayout == null || oldLayout instanceof UIResource || container instanceof JViewport &&
                oldLayout == ReflectUtils.getStaticFieldValueSafely ( ViewportLayout.class, "SHARED_INSTANCE" );
    }

    /**
     * Appends empty property values if required.
     *
     * @param properties property values
     * @return modified property values
     */
    @NotNull
    protected Map<String, Object> appendEmptyComponentProperties ( @NotNull final Map<String, Object> properties )
    {
        if ( !properties.containsKey ( ComponentStyleConverter.MARGIN_ATTRIBUTE ) )
        {
            properties.put ( ComponentStyleConverter.MARGIN_ATTRIBUTE, new InsetsUIResource ( 0, 0, 0, 0 ) );
        }
        if ( !properties.containsKey ( ComponentStyleConverter.PADDING_ATTRIBUTE ) )
        {
            properties.put ( ComponentStyleConverter.PADDING_ATTRIBUTE, new InsetsUIResource ( 0, 0, 0, 0 ) );
        }
        return properties;
    }

    /**
     * Removes this {@link ComponentStyle} from the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove this {@link ComponentStyle} from
     */
    public void remove ( @NotNull final JComponent component )
    {
        try
        {
            // Uninstalling skin painters from the UI
            final ComponentUI ui = getComponentUI ( component );
            PainterSupport.setPainter ( component, ui, null );
        }
        catch ( final Exception e )
        {
            throw new StyleException ( String.format ( "Unable to remove style '%s' from component: %s", getCompleteId (), component ), e );
        }
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     * Note that this method might also work even if there is no real field with the specified name but there is fitting setter method.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    private void setFieldValue ( @NotNull final Object object, @NotNull final String field, @Nullable final Object value )
            throws InvocationTargetException, IllegalAccessException
    {
        // Skipping value if it is marked as ignored
        if ( value != IgnoredValue.VALUE )
        {
            // Creating separate usable value to avoid source object modifications
            // We have limited options here, so for now we simply clone objects which are defined as Cloneable
            final Object usable;
            if ( value instanceof Painter )
            {
                usable = value;
            }
            else
            {
                try
                {
                    usable = Clone.deep ().clone ( value );
                }
                catch ( final Exception e )
                {
                    final String msg = "Unable to clone value: %s";
                    throw new StyleException ( String.format ( msg, value ), e );
                }
            }

            try
            {
                // todo Add more options on the method names here?
                // Trying to use setter method to apply the specified value
                final String setterMethod = ReflectUtils.getSetterMethodName ( field );
                ReflectUtils.callMethod ( object, setterMethod, usable );
            }
            catch ( final NoSuchMethodException e )
            {
                try
                {
                    // Applying field value directly
                    ReflectUtils.setFieldValue ( object, field, usable );
                }
                catch ( final Exception fe )
                {
                    final String msg = "Unable to set `%s` object `%s` field value to: %s";
                    throw new StyleException ( String.format ( msg, object, field, usable ), fe );
                }
            }
        }
    }

    /**
     * Returns component UI object.
     *
     * @param component component instance
     * @return component UI object
     */
    @NotNull
    private ComponentUI getComponentUI ( @NotNull final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( ui == null )
        {
            final String msg = "Unable to retrieve UI from component '%s'";
            throw new StyleException ( String.format ( msg, component ) );
        }
        return ui;
    }

    /**
     * Merges specified style on top of this style.
     *
     * @param style style to merge on top of this one
     * @return merge result
     */
    @NotNull
    public ComponentStyle merge ( @NotNull final ComponentStyle style )
    {
        // Applying new parent
        setParent ( style.getParent () );

        // Applying style identifier from the merged style
        setId ( style.getId () );

        // Applying extended ID from the merged style
        setExtendsId ( style.getExtendsId () );

        // Apply style settings as in case it was extended
        // The same mechanism is used because this is basically an extension of existing style
        extend ( style );

        // Returns merge result
        return this;
    }

    /**
     * Applies specified style settings on top of this style settings.
     *
     * @param style style to merge on top of this one
     * @return current style
     */
    @NotNull
    private ComponentStyle extend ( @NotNull final ComponentStyle style )
    {
        // Copying settings from extended style
        mergeProperties ( getComponentProperties (), style.getComponentProperties () );
        mergeProperties ( getUIProperties (), style.getUIProperties () );
        mergePainters ( this, style );

        // Merging nested styles
        // In case there are no merged styles we don't need to do anything
        final int nestedCount = getStylesCount ();
        final int mergedCount = style.getStylesCount ();
        if ( nestedCount > 0 && mergedCount > 0 )
        {
            // Inherits items that have a parent in a new element, but not in the current
            for ( final ComponentStyle child : getNestedStyles () )
            {
                extendChild ( style, child );
            }

            // Merge styles
            final List<ComponentStyle> nestedStyles = getNestedStyles ();
            for ( final ComponentStyle mergedNestedStyle : style.getNestedStyles () )
            {
                // Looking for existing style with the same ID and type
                ComponentStyle existing = null;
                for ( final ComponentStyle nestedStyle : getNestedStyles () )
                {
                    if ( Objects.equals ( mergedNestedStyle.getType (), nestedStyle.getType () ) &&
                            Objects.equals ( mergedNestedStyle.getId (), nestedStyle.getId () ) )
                    {
                        existing = nestedStyle;
                        break;
                    }
                }

                // Either merging style with existing one or simply saving clone
                if ( existing != null )
                {
                    // Merging existing nested style and moving it to the end
                    nestedStyles.remove ( existing );
                    existing.extend ( mergedNestedStyle );
                    nestedStyles.add ( existing );
                }
                else
                {
                    // Simply using merged style clone
                    final ComponentStyle mergedNestedStyleClone = mergedNestedStyle.clone ();
                    mergedNestedStyleClone.setParent ( ComponentStyle.this );
                    nestedStyles.add ( mergedNestedStyleClone );
                }
            }
            setNestedStyles ( nestedStyles );
        }
        else if ( mergedCount > 0 )
        {
            // Simply set merged styles
            final List<ComponentStyle> mergedStylesClone = Clone.deep ().nonNullClone ( style.getNestedStyles () );
            for ( final ComponentStyle mergedStyleClone : mergedStylesClone )
            {
                mergedStyleClone.setParent ( this );
            }
            setNestedStyles ( mergedStylesClone );
        }
        else if ( nestedCount > 0 )
        {
            // Simply set base styles
            final List<ComponentStyle> baseStylesClone = Clone.deep ().nonNullClone ( getNestedStyles () );
            for ( final ComponentStyle baseStyleClone : baseStylesClone )
            {
                baseStyleClone.setParent ( this );
            }
            setNestedStyles ( baseStylesClone );
        }
        return this;
    }

    /**
     * Inherits items that have a parent in a new element, but not in the current
     *
     * @param style component style that is merged on top of this one
     * @param child this component style child
     */
    private void extendChild ( @NotNull final ComponentStyle style, @NotNull final ComponentStyle child )
    {
        // Skipping styles that are already provided in style merged on top of this
        for ( final ComponentStyle newParentChild : style.getNestedStyles () )
        {
            if ( child.getId ().equals ( newParentChild.getId () ) )
            {
                return;
            }
        }

        // Overriding provided style with this style child
        for ( final ComponentStyle mergedChild : style.getNestedStyles () )
        {
            if ( Objects.equals ( child.getExtendsId (), mergedChild.getId () ) )
            {
                style.getNestedStyles ().add ( child.clone ().extend ( mergedChild ) );
                return;
            }
        }
    }

    /**
     * Merges two {@link PainterStyle} settings together.
     *
     * @param style       extended {@link ComponentStyle}
     * @param mergedStyle merged {@link ComponentStyle}
     */
    private void mergePainters ( @NotNull final ComponentStyle style, @NotNull final ComponentStyle mergedStyle )
    {
        final PainterStyle resultPainterStyle;

        // Resolving resulting style
        final PainterStyle painterStyle = style.getPainterStyle ();
        final PainterStyle mergedPainterStyle = mergedStyle.getPainterStyle ();
        if ( painterStyle != null )
        {
            if ( mergedPainterStyle != null )
            {
                // Copying painter properties if extended painter class type is assignable from current painter class type
                final Class painterClass = ReflectUtils.getClassSafely ( mergedPainterStyle.getPainterClass () );
                final Class extendedPainterClass = ReflectUtils.getClassSafely ( painterStyle.getPainterClass () );
                if ( painterClass == null || extendedPainterClass == null )
                {
                    final String pc = painterClass == null ? mergedPainterStyle.getPainterClass () : painterStyle.getPainterClass ();
                    final String msg = "Component style '%s' points to a missing painter class: %s";
                    throw new StyleException ( String.format ( msg, mergedStyle, pc ) );
                }
                if ( ReflectUtils.isAssignable ( extendedPainterClass, painterClass ) )
                {
                    // Adding painter styles merge result
                    painterStyle.setPainterClass ( mergedPainterStyle.getPainterClass () );
                    mergeProperties ( painterStyle.getProperties (), mergedPainterStyle.getProperties () );
                    resultPainterStyle = painterStyle;
                }
                else
                {
                    // Adding a full copy of merged painter style
                    resultPainterStyle = Clone.deep ().nonNullClone ( mergedPainterStyle );
                }
            }
            else
            {
                // Adding a full copy of base painter style
                resultPainterStyle = Clone.deep ().nonNullClone ( painterStyle );
            }
        }
        else
        {
            if ( mergedPainterStyle != null )
            {
                // Adding a full copy of merged painter style
                resultPainterStyle = Clone.deep ().nonNullClone ( mergedPainterStyle );
            }
            else
            {
                // No painter style
                resultPainterStyle = null;
            }
        }

        // Updating painters list
        style.setPainterStyle ( resultPainterStyle );
    }

    /**
     * Performs properties merge.
     *
     * @param properties base properties
     * @param merged     merged properties
     */
    private void mergeProperties ( @NotNull final Map<String, Object> properties, @NotNull final Map<String, Object> merged )
    {
        for ( final Map.Entry<String, Object> property : merged.entrySet () )
        {
            final String key = property.getKey ();
            final Object existingValue = properties.get ( key );
            final Object mergedValue = property.getValue ();

            try
            {
                // Merging value on top of existing one
                final Object result = Merge.deep ().merge ( existingValue, mergedValue );

                // Saving merge result
                properties.put ( key, result );
            }
            catch ( final Exception ex )
            {
                final String msg = "Unable to merge property '%s' values: '%s' and '%s'";
                LoggerFactory.getLogger ( ComponentStyle.class ).error ( String.format ( msg, key, existingValue, mergedValue ), ex );
            }
        }
    }

    @NotNull
    @Override
    public ComponentStyle clone ( @NotNull final RecursiveClone clone, final int depth )
    {
        final ComponentStyle styleCopy = clone.cloneFields ( this, depth );

        /**
         * Updating transient parent field for child {@link ComponentStyle}s.
         * This is important since their parent have been cloned and needs to be updated.
         * Zero depth cloned object doesn't need parent to be updated, it is preserved upon clone.
         */
        if ( CollectionUtils.notEmpty ( styleCopy.getNestedStyles () ) )
        {
            for ( final ComponentStyle styleCopyChild : styleCopy.getNestedStyles () )
            {
                styleCopyChild.setParent ( styleCopy );
            }
        }

        return styleCopy;
    }

    @NotNull
    @Override
    public ComponentStyle clone ()
    {
        return Clone.deep ().nonNullClone ( this );
    }

    @NotNull
    @Override
    public String toString ()
    {
        return "ComponentStyle [ id: " + getCompleteId () + " ]";
    }
}