package de.tesis.dynaware.grapheditor.demo.customskins.state.machine;

import de.tesis.dynaware.grapheditor.GConnectorValidator;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.TreeSkinConstants;
import de.tesis.dynaware.grapheditor.model.GConnector;

public class StateMachineConnectorValidator implements GConnectorValidator {

    @Override
    public boolean prevalidate(final GConnector source, final GConnector target) {

        if (source == null || target == null) {
            return false;
        } else if (source.equals(target)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validate(final GConnector source, final GConnector target) {

        if (source.getType() == null || target.getType() == null) {
            return false;
        } else if (source.getParent().equals(target.getParent())) {
            return false;
        } else if (source.getType().equals(target.getType())) {
            return false;
        }

        return true;
    }

    @Override
    public String createConnectionType(final GConnector source, final GConnector target) {
        //////System.out.println("creates a state machine conenctions");
        return StateMachineConstants.STATE_MACHINE_CONNECTION;
    }

    @Override
    public String createJointType(final GConnector source, final GConnector target) {
        return null;
    }
}
