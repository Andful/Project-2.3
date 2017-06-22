package Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

public class EnvironmentFloat{

	public Vector3f environmentSize;
	public List<Vector3f> obstaclesPositions;
	public List<Vector3f> agentStartConfigurations;
	public List<Vector3f> agentEndConfigurations;
	
	public EnvironmentFloat(File environment, File obstacles, File configurations) throws IOException{
		
		try(DataInputStream in = new DataInputStream(new FileInputStream(environment))) {
			
			environmentSize = new Vector3f(in.readFloat(), in.readFloat(), in.readFloat());
			
		}
		try(DataInputStream in = new DataInputStream(new FileInputStream(obstacles))){
			final int size=in.readInt();
			obstaclesPositions = new ArrayList<Vector3f>(size);
			for(int i=0;i<size;i++)
			{
				obstaclesPositions.add(new Vector3f(in.readFloat(),in.readFloat(),in.readFloat()));
			}
			
		}
		try(DataInputStream in = new DataInputStream(new FileInputStream(configurations)))
		{
			int sizeStartConf = in.readInt();
			agentStartConfigurations = new ArrayList<Vector3f>(sizeStartConf);
			for(int i=0; i<sizeStartConf;i++)
			{
				agentStartConfigurations.add(new Vector3f(in.readFloat(),in.readFloat(),in.readFloat()));
			}

			int sizeEndConf = in.readInt();
			agentEndConfigurations = new ArrayList<Vector3f>(sizeEndConf);
			for(int i=0; i<sizeEndConf;i++){

				agentEndConfigurations.add(new Vector3f(in.readFloat(),in.readFloat(),in.readFloat()));

			}

		}
		
	}

	public EnvironmentFloat(File directory) throws IOException
	{
		this(new File(directory.toString() + File.separator + "enviroment size.project"),
				new File(directory.toString() + File.separator + "obstacles.project"),
				new File(directory.toString() + File.separator + "configuration.project"));
	}

	public EnvironmentFloat(Vector3f environmentSize, List<Vector3f> obstaclesPositions, List<Vector3f> agentStartConfigurations, List<Vector3f> agentEndConfigurations){
		this.environmentSize = environmentSize;
		this.obstaclesPositions = obstaclesPositions;
		this.agentEndConfigurations = agentEndConfigurations;
		this.agentStartConfigurations = agentStartConfigurations;
	}

	public void environmentToFile(File environment, File obstacles, File configurations) throws IOException{

		try(DataOutputStream out = new DataOutputStream(new FileOutputStream(environment))){

			out.writeFloat(environmentSize.x);
			out.writeFloat(environmentSize.y);
			out.writeFloat(environmentSize.z);
		}
		try(DataOutputStream out = new DataOutputStream(new FileOutputStream(obstacles))){

			out.writeInt(obstaclesPositions.size());

			for(Vector3f vec:obstaclesPositions)
			{
				out.writeFloat(vec.x);
				out.writeFloat(vec.y);
				out.writeFloat(vec.z);
			}
		}
		try(DataOutputStream out = new DataOutputStream(new FileOutputStream(configurations)))
		{

			out.writeInt(agentStartConfigurations.size());

			for(Vector3f vec:agentStartConfigurations){
				out.writeFloat(vec.x);
				out.writeFloat(vec.y);
				out.writeFloat(vec.z);
			}

			out.writeInt(agentEndConfigurations.size());

			for(Vector3f vec:agentEndConfigurations){
				out.writeFloat(vec.x);
				out.writeFloat(vec.y);
				out.writeFloat(vec.z);
			}

		}
	}
	public static void main(String[] args)
	{
		//testWrite(args);
		testRead(args);
	}
	public static void testWrite(String[] args)
	{
		Vector3f enviromentSize=new Vector3f(4,5,6);
		List<Vector3f> obstacles=new ArrayList();
		obstacles.add(new Vector3f(1,2,3));
		obstacles.add(new Vector3f(4,5,6));
		List<Vector3f> start=new ArrayList<>();
		start.add(new Vector3f(7,8,9));
		start.add(new Vector3f(10,11,12));
		List<Vector3f> end=new ArrayList<>();
		end.add(new Vector3f(13,14,15));
		end.add(new Vector3f(16,17,18));
		EnvironmentFloat env=new EnvironmentFloat(enviromentSize,obstacles,start,end);
		try
		{
			env.environmentToFile(new File("C:\\Users\\Andrea Nardi\\Desktop\\test\\a"),
					new File("C:\\Users\\Andrea Nardi\\Desktop\\test\\b"),
					new File("C:\\Users\\Andrea Nardi\\Desktop\\test\\c"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void testRead(String[] args){
		EnvironmentFloat env = null;
		try{
			env = new EnvironmentFloat(new File("C:\\Users\\Andrea Nardi\\Desktop\\test\\a"),
					new File("C:\\Users\\Andrea Nardi\\Desktop\\test\\b"),
					new File("C:\\Users\\Andrea Nardi\\Desktop\\test\\c"));
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println(env.environmentSize);
		System.out.println(env.obstaclesPositions);
		System.out.println(env.agentStartConfigurations);
		System.out.println(env.agentEndConfigurations);
	}
}
