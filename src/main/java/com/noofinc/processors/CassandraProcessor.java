package com.noofinc.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cassandra.db.marshal.UTF8Type;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.RandomPartitioner;
import org.apache.cassandra.io.sstable.SSTableSimpleUnsortedWriter;

import static org.apache.cassandra.utils.ByteBufferUtil.bytes;

public class CassandraProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		String keySpace = "Demo";
		File directory = new File(keySpace);
		if (!directory.exists()) {
			directory.mkdir();
		}

		IPartitioner partitioner = new RandomPartitioner();
		SSTableSimpleUnsortedWriter writer = new SSTableSimpleUnsortedWriter(
				directory, partitioner, keySpace, "stuff", UTF8Type.instance,
				null, 20);

		String line;
		int lineNumber = 1;
		long timestamp = System.currentTimeMillis() * 1000;

		while (lineNumber < 500000) {
			writer.newRow(bytes(UUID.randomUUID()));
			writer.addColumn(bytes("one"), bytes(1234124), timestamp);
			writer.addColumn(bytes("two"), bytes(5555), timestamp);
			writer.addColumn(bytes("three"), bytes(444), timestamp);
			writer.addColumn(bytes("four"), bytes(21), timestamp);
			writer.addColumn(bytes("five"), bytes(12), timestamp);
			lineNumber++;
		}
		writer.close();

	}

}
