package yget.impls;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import yget.interfaces.URLFileParser;

public class URLFileParseUtility implements URLFileParser
{
	@Override
	public String[] parse(String pathToFile)
	{
		String[] result = null;
		String content = "";

		try (BufferedReader br = new BufferedReader(new FileReader(pathToFile)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				content += line + "\n";
			}
			result = content.split("\n");

		}
		catch (IOException e)
		{
			return null;
		}

		return result;
	}

}
