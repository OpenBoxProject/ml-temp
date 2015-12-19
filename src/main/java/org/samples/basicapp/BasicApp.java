package org.samples.basicapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.moonlightcontroller.bal.BoxApplication;
import org.moonlightcontroller.blocks.FromDevice;
import org.moonlightcontroller.blocks.HeaderClassifier;
import org.moonlightcontroller.blocks.HeaderClassifier.HeaderClassifierRule;
import org.moonlightcontroller.blocks.ToDevice;
import org.moonlightcontroller.processing.Connector;
import org.openboxprotocol.protocol.HeaderField;
import org.openboxprotocol.protocol.HeaderMatch;
import org.openboxprotocol.protocol.IStatement;
import org.openboxprotocol.protocol.OpenBoxHeaderMatch;
import org.openboxprotocol.protocol.Priority;
import org.openboxprotocol.protocol.Statement;
import org.openboxprotocol.protocol.topology.InstanceLocationSpecifier;
import org.openboxprotocol.types.TransportPort;

public class BasicApp extends BoxApplication {

	public BasicApp() {
		super("The most basic app in the world", Priority.HIGH);
		List<IStatement> statements = createStatements();
		System.out.println("KOKO" + statements.get(0).getLocation().getId());
		this.setStatements(statements);
	}
	
	private List<IStatement> createStatements() {
		
		HeaderMatch h1 = new OpenBoxHeaderMatch.Builder().setExact(HeaderField.TCP_DST, new TransportPort(22)).build();
		HeaderMatch h2 = new OpenBoxHeaderMatch.Builder().setExact(HeaderField.TCP_DST, TransportPort.ANY).build();
		ArrayList<IStatement> statements = new ArrayList<>();
		FromDevice from = new FromDevice.Builder().setDevice("eth0").setPromisc(true).setSniffer(true).build();
		ToDevice to1 = new ToDevice.Builder().setDevice("eth1").build();
		ToDevice to2 = new ToDevice.Builder().setDevice("eth2").build();
		HeaderClassifier classify = new HeaderClassifier.Builder()
				.setRules(new ArrayList<HeaderClassifierRule>(Arrays.asList(
						new HeaderClassifierRule.Builder().setHeaderMatch(h1).setPriority(Priority.HIGH).setOrder(0).build(),
						new HeaderClassifierRule.Builder().setHeaderMatch(h2).setPriority(Priority.HIGH).setOrder(1).build())))
				.setPriority(Priority.HIGH)
				.build();
		IStatement st = new Statement.Builder()
			.setLocation(new InstanceLocationSpecifier("ep1", 1000))
			.addBlock(from)
			.addBlock(to1)
			.addBlock(classify)
			.addConnector(new Connector.Builder()
				.setSourceBlock(from)
				.setSourceOutputPort(from.getOutputPort())
				.setDestBlock(classify)
				.build())
			.addConnector(new Connector.Builder()
				.setSourceBlock(classify)
				.setSourceOutputPort(0)
				.setDestBlock(to1)
				.build())
			.addConnector(new Connector.Builder()
				.setSourceBlock(classify)
				.setSourceOutputPort(1)
				.setDestBlock(to2)
				.build())
			.build();
		statements.add(st);
		return statements;
	}
}
