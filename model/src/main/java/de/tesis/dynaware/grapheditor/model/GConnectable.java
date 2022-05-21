/**
 */
package de.tesis.dynaware.grapheditor.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GConnectable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link model.GConnectable#getConnectors <em>Connectors</em>}</li>
 * </ul>
 *
 * @see model.ModelPackage#getGConnectable()
 * @model abstract="true"
 * @generated
 */
public interface GConnectable extends EObject {
	/**
	 * Returns the value of the '<em><b>Connectors</b></em>' containment reference list.
	 * The list contents are of type {@link model.GConnector}.
	 * It is bidirectional and its opposite is '{@link model.GConnector#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connectors</em>' containment reference list.
	 * @see model.ModelPackage#getGConnectable_Connectors()
	 * @see model.GConnector#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<GConnector> getConnectors();

} // GConnectable
