/**
 */
package de.tesis.dynaware.grapheditor.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GConnection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link model.GConnection#getId <em>Id</em>}</li>
 *   <li>{@link model.GConnection#getType <em>Type</em>}</li>
 *   <li>{@link model.GConnection#getSource <em>Source</em>}</li>
 *   <li>{@link model.GConnection#getTarget <em>Target</em>}</li>
 *   <li>{@link model.GConnection#getJoints <em>Joints</em>}</li>
 * </ul>
 *
 * @see model.ModelPackage#getGConnection()
 * @model
 * @generated
 */
public interface GConnection extends GConnectable {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see model.ModelPackage#getGConnection_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link model.GConnection#getId <em>Id</em>}' attribute.
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
	 * @see model.ModelPackage#getGConnection_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link model.GConnection#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(GConnector)
	 * @see model.ModelPackage#getGConnection_Source()
	 * @model required="true"
	 * @generated
	 */
	GConnector getSource();

	/**
	 * Sets the value of the '{@link model.GConnection#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(GConnector value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(GConnector)
	 * @see model.ModelPackage#getGConnection_Target()
	 * @model required="true"
	 * @generated
	 */
	GConnector getTarget();

	/**
	 * Sets the value of the '{@link model.GConnection#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(GConnector value);

	/**
	 * Returns the value of the '<em><b>Joints</b></em>' containment reference list.
	 * The list contents are of type {@link model.GJoint}.
	 * It is bidirectional and its opposite is '{@link model.GJoint#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Joints</em>' containment reference list.
	 * @see model.ModelPackage#getGConnection_Joints()
	 * @see model.GJoint#getConnection
	 * @model opposite="connection" containment="true"
	 * @generated
	 */
	EList<GJoint> getJoints();

} // GConnection
