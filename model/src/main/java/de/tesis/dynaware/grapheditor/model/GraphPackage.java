/**
 */
package de.tesis.dynaware.grapheditor.model;

import de.tesis.dynaware.grapheditor.model.impl.GraphPackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface GraphPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://de.tesis.dynaware.grapheditor.model/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "graph";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GraphPackage eINSTANCE = GraphPackageImpl.init();

	/**
	 * The meta object id for the '{@link de.tesis.dynaware.grapheditor.model.impl.GModelImpl <em>GModel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.tesis.dynaware.grapheditor.model.impl.GModelImpl
	 * @see GraphPackageImpl#getGModel()
	 * @generated
	 */
	int GMODEL = 0;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL__NODES = 0;

	/**
	 * The feature id for the '<em><b>Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL__CONNECTIONS = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL__TYPE = 2;

	/**
	 * The feature id for the '<em><b>Content Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL__CONTENT_WIDTH = 3;

	/**
	 * The feature id for the '<em><b>Content Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL__CONTENT_HEIGHT = 4;

	/**
	 * The feature id for the '<em><b>Supergraph</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL__SUPERGRAPH = 5;

	/**
	 * The number of structural features of the '<em>GModel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>GModel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GMODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link model.impl.GConnectableImpl <em>GConnectable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.GConnectableImpl
	 * @see model.impl.ModelPackageImpl#getGConnectable()
	 * @generated
	 */
	int GCONNECTABLE = 1;

	/**
	 * The feature id for the '<em><b>Connectors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTABLE__CONNECTORS = 0;

	/**
	 * The number of structural features of the '<em>GConnectable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTABLE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>GConnectable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTABLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link model.impl.GNodeImpl <em>GNode</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.GNodeImpl
	 * @see model.impl.ModelPackageImpl#getGNode()
	 * @generated
	 */
	int GNODE = 2;

	/**
	 * The feature id for the '<em><b>Connectors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__CONNECTORS = GCONNECTABLE__CONNECTORS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__ID = GCONNECTABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__TYPE = GCONNECTABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__X = GCONNECTABLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__Y = GCONNECTABLE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__WIDTH = GCONNECTABLE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__HEIGHT = GCONNECTABLE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Subgraph</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__SUBGRAPH = GCONNECTABLE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE__DESCRIPTION = GCONNECTABLE_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>GNode</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE_FEATURE_COUNT = GCONNECTABLE_FEATURE_COUNT + 8;

	/**
	 * The number of operations of the '<em>GNode</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GNODE_OPERATION_COUNT = GCONNECTABLE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link model.impl.GConnectionImpl <em>GConnection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.GConnectionImpl
	 * @see model.impl.ModelPackageImpl#getGConnection()
	 * @generated
	 */
	int GCONNECTION = 3;

	/**
	 * The feature id for the '<em><b>Connectors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION__CONNECTORS = GCONNECTABLE__CONNECTORS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION__ID = GCONNECTABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION__TYPE = GCONNECTABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION__SOURCE = GCONNECTABLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION__TARGET = GCONNECTABLE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Joints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION__JOINTS = GCONNECTABLE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>GConnection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION_FEATURE_COUNT = GCONNECTABLE_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>GConnection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTION_OPERATION_COUNT = GCONNECTABLE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link model.impl.GConnectorImpl <em>GConnector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.GConnectorImpl
	 * @see model.impl.ModelPackageImpl#getGConnector()
	 * @generated
	 */
	int GCONNECTOR = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__ID = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__PARENT = 2;

	/**
	 * The feature id for the '<em><b>Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__CONNECTIONS = 3;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__X = 4;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__Y = 5;

	/**
	 * The feature id for the '<em><b>Connection Detached On Drag</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR__CONNECTION_DETACHED_ON_DRAG = 6;

	/**
	 * The number of structural features of the '<em>GConnector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>GConnector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GCONNECTOR_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link model.impl.GJointImpl <em>GJoint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.GJointImpl
	 * @see model.impl.ModelPackageImpl#getGJoint()
	 * @generated
	 */
	int GJOINT = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT__ID = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT__CONNECTION = 2;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT__X = 3;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT__Y = 4;

	/**
	 * The number of structural features of the '<em>GJoint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>GJoint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GJOINT_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link model.GModel <em>GModel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GModel</em>'.
	 * @see model.GModel
	 * @generated
	 */
	EClass getGModel();

	/**
	 * Returns the meta object for the containment reference list '{@link model.GModel#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see model.GModel#getNodes()
	 * @see #getGModel()
	 * @generated
	 */
	EReference getGModel_Nodes();

	/**
	 * Returns the meta object for the containment reference list '{@link model.GModel#getConnections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connections</em>'.
	 * @see model.GModel#getConnections()
	 * @see #getGModel()
	 * @generated
	 */
	EReference getGModel_Connections();

	/**
	 * Returns the meta object for the attribute '{@link model.GModel#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see model.GModel#getType()
	 * @see #getGModel()
	 * @generated
	 */
	EAttribute getGModel_Type();

	/**
	 * Returns the meta object for the attribute '{@link model.GModel#getContentWidth <em>Content Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content Width</em>'.
	 * @see model.GModel#getContentWidth()
	 * @see #getGModel()
	 * @generated
	 */
	EAttribute getGModel_ContentWidth();

	/**
	 * Returns the meta object for the attribute '{@link model.GModel#getContentHeight <em>Content Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content Height</em>'.
	 * @see model.GModel#getContentHeight()
	 * @see #getGModel()
	 * @generated
	 */
	EAttribute getGModel_ContentHeight();

	/**
	 * Returns the meta object for the container reference '{@link model.GModel#getSupergraph <em>Supergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Supergraph</em>'.
	 * @see model.GModel#getSupergraph()
	 * @see #getGModel()
	 * @generated
	 */
	EReference getGModel_Supergraph();

	/**
	 * Returns the meta object for class '{@link model.GConnectable <em>GConnectable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GConnectable</em>'.
	 * @see model.GConnectable
	 * @generated
	 */
	EClass getGConnectable();

	/**
	 * Returns the meta object for the containment reference list '{@link model.GConnectable#getConnectors <em>Connectors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connectors</em>'.
	 * @see model.GConnectable#getConnectors()
	 * @see #getGConnectable()
	 * @generated
	 */
	EReference getGConnectable_Connectors();

	/**
	 * Returns the meta object for class '{@link model.GNode <em>GNode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GNode</em>'.
	 * @see model.GNode
	 * @generated
	 */
	EClass getGNode();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see model.GNode#getId()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_Id();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see model.GNode#getType()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_Type();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see model.GNode#getX()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_X();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see model.GNode#getY()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_Y();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see model.GNode#getWidth()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_Width();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see model.GNode#getHeight()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_Height();

	/**
	 * Returns the meta object for the containment reference '{@link model.GNode#getSubgraph <em>Subgraph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Subgraph</em>'.
	 * @see model.GNode#getSubgraph()
	 * @see #getGNode()
	 * @generated
	 */
	EReference getGNode_Subgraph();

	/**
	 * Returns the meta object for the attribute '{@link model.GNode#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see model.GNode#getDescription()
	 * @see #getGNode()
	 * @generated
	 */
	EAttribute getGNode_Description();

	/**
	 * Returns the meta object for class '{@link model.GConnection <em>GConnection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GConnection</em>'.
	 * @see model.GConnection
	 * @generated
	 */
	EClass getGConnection();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnection#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see model.GConnection#getId()
	 * @see #getGConnection()
	 * @generated
	 */
	EAttribute getGConnection_Id();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnection#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see model.GConnection#getType()
	 * @see #getGConnection()
	 * @generated
	 */
	EAttribute getGConnection_Type();

	/**
	 * Returns the meta object for the reference '{@link model.GConnection#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see model.GConnection#getSource()
	 * @see #getGConnection()
	 * @generated
	 */
	EReference getGConnection_Source();

	/**
	 * Returns the meta object for the reference '{@link model.GConnection#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see model.GConnection#getTarget()
	 * @see #getGConnection()
	 * @generated
	 */
	EReference getGConnection_Target();

	/**
	 * Returns the meta object for the containment reference list '{@link model.GConnection#getJoints <em>Joints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Joints</em>'.
	 * @see model.GConnection#getJoints()
	 * @see #getGConnection()
	 * @generated
	 */
	EReference getGConnection_Joints();

	/**
	 * Returns the meta object for class '{@link model.GConnector <em>GConnector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GConnector</em>'.
	 * @see model.GConnector
	 * @generated
	 */
	EClass getGConnector();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnector#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see model.GConnector#getId()
	 * @see #getGConnector()
	 * @generated
	 */
	EAttribute getGConnector_Id();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnector#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see model.GConnector#getType()
	 * @see #getGConnector()
	 * @generated
	 */
	EAttribute getGConnector_Type();

	/**
	 * Returns the meta object for the container reference '{@link model.GConnector#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see model.GConnector#getParent()
	 * @see #getGConnector()
	 * @generated
	 */
	EReference getGConnector_Parent();

	/**
	 * Returns the meta object for the reference list '{@link model.GConnector#getConnections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Connections</em>'.
	 * @see model.GConnector#getConnections()
	 * @see #getGConnector()
	 * @generated
	 */
	EReference getGConnector_Connections();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnector#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see model.GConnector#getX()
	 * @see #getGConnector()
	 * @generated
	 */
	EAttribute getGConnector_X();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnector#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see model.GConnector#getY()
	 * @see #getGConnector()
	 * @generated
	 */
	EAttribute getGConnector_Y();

	/**
	 * Returns the meta object for the attribute '{@link model.GConnector#isConnectionDetachedOnDrag <em>Connection Detached On Drag</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Connection Detached On Drag</em>'.
	 * @see model.GConnector#isConnectionDetachedOnDrag()
	 * @see #getGConnector()
	 * @generated
	 */
	EAttribute getGConnector_ConnectionDetachedOnDrag();

	/**
	 * Returns the meta object for class '{@link model.GJoint <em>GJoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GJoint</em>'.
	 * @see model.GJoint
	 * @generated
	 */
	EClass getGJoint();

	/**
	 * Returns the meta object for the attribute '{@link model.GJoint#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see model.GJoint#getId()
	 * @see #getGJoint()
	 * @generated
	 */
	EAttribute getGJoint_Id();

	/**
	 * Returns the meta object for the attribute '{@link model.GJoint#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see model.GJoint#getType()
	 * @see #getGJoint()
	 * @generated
	 */
	EAttribute getGJoint_Type();

	/**
	 * Returns the meta object for the container reference '{@link model.GJoint#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Connection</em>'.
	 * @see model.GJoint#getConnection()
	 * @see #getGJoint()
	 * @generated
	 */
	EReference getGJoint_Connection();

	/**
	 * Returns the meta object for the attribute '{@link model.GJoint#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see model.GJoint#getX()
	 * @see #getGJoint()
	 * @generated
	 */
	EAttribute getGJoint_X();

	/**
	 * Returns the meta object for the attribute '{@link model.GJoint#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see model.GJoint#getY()
	 * @see #getGJoint()
	 * @generated
	 */
	EAttribute getGJoint_Y();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GraphFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link model.impl.GModelImpl <em>GModel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.GModelImpl
		 * @see model.impl.ModelPackageImpl#getGModel()
		 * @generated
		 */
		EClass GMODEL = eINSTANCE.getGModel();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GMODEL__NODES = eINSTANCE.getGModel_Nodes();

		/**
		 * The meta object literal for the '<em><b>Connections</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GMODEL__CONNECTIONS = eINSTANCE.getGModel_Connections();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GMODEL__TYPE = eINSTANCE.getGModel_Type();

		/**
		 * The meta object literal for the '<em><b>Content Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GMODEL__CONTENT_WIDTH = eINSTANCE.getGModel_ContentWidth();

		/**
		 * The meta object literal for the '<em><b>Content Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GMODEL__CONTENT_HEIGHT = eINSTANCE.getGModel_ContentHeight();

		/**
		 * The meta object literal for the '<em><b>Supergraph</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GMODEL__SUPERGRAPH = eINSTANCE.getGModel_Supergraph();

		/**
		 * The meta object literal for the '{@link model.impl.GConnectableImpl <em>GConnectable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.GConnectableImpl
		 * @see model.impl.ModelPackageImpl#getGConnectable()
		 * @generated
		 */
		EClass GCONNECTABLE = eINSTANCE.getGConnectable();

		/**
		 * The meta object literal for the '<em><b>Connectors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GCONNECTABLE__CONNECTORS = eINSTANCE.getGConnectable_Connectors();

		/**
		 * The meta object literal for the '{@link model.impl.GNodeImpl <em>GNode</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.GNodeImpl
		 * @see model.impl.ModelPackageImpl#getGNode()
		 * @generated
		 */
		EClass GNODE = eINSTANCE.getGNode();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__ID = eINSTANCE.getGNode_Id();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__TYPE = eINSTANCE.getGNode_Type();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__X = eINSTANCE.getGNode_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__Y = eINSTANCE.getGNode_Y();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__WIDTH = eINSTANCE.getGNode_Width();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__HEIGHT = eINSTANCE.getGNode_Height();

		/**
		 * The meta object literal for the '<em><b>Subgraph</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GNODE__SUBGRAPH = eINSTANCE.getGNode_Subgraph();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GNODE__DESCRIPTION = eINSTANCE.getGNode_Description();

		/**
		 * The meta object literal for the '{@link model.impl.GConnectionImpl <em>GConnection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.GConnectionImpl
		 * @see model.impl.ModelPackageImpl#getGConnection()
		 * @generated
		 */
		EClass GCONNECTION = eINSTANCE.getGConnection();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTION__ID = eINSTANCE.getGConnection_Id();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTION__TYPE = eINSTANCE.getGConnection_Type();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GCONNECTION__SOURCE = eINSTANCE.getGConnection_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GCONNECTION__TARGET = eINSTANCE.getGConnection_Target();

		/**
		 * The meta object literal for the '<em><b>Joints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GCONNECTION__JOINTS = eINSTANCE.getGConnection_Joints();

		/**
		 * The meta object literal for the '{@link model.impl.GConnectorImpl <em>GConnector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.GConnectorImpl
		 * @see model.impl.ModelPackageImpl#getGConnector()
		 * @generated
		 */
		EClass GCONNECTOR = eINSTANCE.getGConnector();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTOR__ID = eINSTANCE.getGConnector_Id();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTOR__TYPE = eINSTANCE.getGConnector_Type();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GCONNECTOR__PARENT = eINSTANCE.getGConnector_Parent();

		/**
		 * The meta object literal for the '<em><b>Connections</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GCONNECTOR__CONNECTIONS = eINSTANCE.getGConnector_Connections();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTOR__X = eINSTANCE.getGConnector_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTOR__Y = eINSTANCE.getGConnector_Y();

		/**
		 * The meta object literal for the '<em><b>Connection Detached On Drag</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GCONNECTOR__CONNECTION_DETACHED_ON_DRAG = eINSTANCE.getGConnector_ConnectionDetachedOnDrag();

		/**
		 * The meta object literal for the '{@link model.impl.GJointImpl <em>GJoint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.GJointImpl
		 * @see model.impl.ModelPackageImpl#getGJoint()
		 * @generated
		 */
		EClass GJOINT = eINSTANCE.getGJoint();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GJOINT__ID = eINSTANCE.getGJoint_Id();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GJOINT__TYPE = eINSTANCE.getGJoint_Type();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GJOINT__CONNECTION = eINSTANCE.getGJoint_Connection();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GJOINT__X = eINSTANCE.getGJoint_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GJOINT__Y = eINSTANCE.getGJoint_Y();

	}

} //GraphPackage
