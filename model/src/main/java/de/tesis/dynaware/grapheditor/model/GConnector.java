/**
 */
package de.tesis.dynaware.grapheditor.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GConnector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link model.GConnector#getId <em>Id</em>}</li>
 *   <li>{@link model.GConnector#getType <em>Type</em>}</li>
 *   <li>{@link model.GConnector#getParent <em>Parent</em>}</li>
 *   <li>{@link model.GConnector#getConnections <em>Connections</em>}</li>
 *   <li>{@link model.GConnector#getX <em>X</em>}</li>
 *   <li>{@link model.GConnector#getY <em>Y</em>}</li>
 *   <li>{@link model.GConnector#isConnectionDetachedOnDrag <em>Connection Detached On Drag</em>}</li>
 * </ul>
 *
 * @see model.ModelPackage#getGConnector()
 * @model
 * @generated
 */
public interface GConnector extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see model.ModelPackage#getGConnector_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link model.GConnector#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see model.ModelPackage#getGConnector_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link model.GConnector#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link model.GConnectable#getConnectors <em>Connectors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(GConnectable)
	 * @see model.ModelPackage#getGConnector_Parent()
	 * @see model.GConnectable#getConnectors
	 * @model opposite="connectors" required="true" transient="false"
	 * @generated
	 */
	GConnectable getParent();

	/**
	 * Sets the value of the '{@link model.GConnector#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(GConnectable value);

	/**
	 * Returns the value of the '<em><b>Connections</b></em>' reference list.
	 * The list contents are of type {@link model.GConnection}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connections</em>' reference list.
	 * @see model.ModelPackage#getGConnector_Connections()
	 * @model
	 * @generated
	 */
	EList<GConnection> getConnections();

	/**
	 * Returns the value of the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>X</em>' attribute.
	 * @see #setX(double)
	 * @see model.ModelPackage#getGConnector_X()
	 * @model
	 * @generated
	 */
	double getX();

	/**
	 * Sets the value of the '{@link model.GConnector#getX <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>X</em>' attribute.
	 * @see #getX()
	 * @generated
	 */
	void setX(double value);

	/**
	 * Returns the value of the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Y</em>' attribute.
	 * @see #setY(double)
	 * @see model.ModelPackage#getGConnector_Y()
	 * @model
	 * @generated
	 */
	double getY();

	/**
	 * Sets the value of the '{@link model.GConnector#getY <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y</em>' attribute.
	 * @see #getY()
	 * @generated
	 */
	void setY(double value);

	/**
	 * Returns the value of the '<em><b>Connection Detached On Drag</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Detached On Drag</em>' attribute.
	 * @see #setConnectionDetachedOnDrag(boolean)
	 * @see model.ModelPackage#getGConnector_ConnectionDetachedOnDrag()
	 * @model default="true" required="true"
	 * @generated
	 */
	boolean isConnectionDetachedOnDrag();

	/**
	 * Sets the value of the '{@link model.GConnector#isConnectionDetachedOnDrag <em>Connection Detached On Drag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection Detached On Drag</em>' attribute.
	 * @see #isConnectionDetachedOnDrag()
	 * @generated
	 */
	void setConnectionDetachedOnDrag(boolean value);

} // GConnector
