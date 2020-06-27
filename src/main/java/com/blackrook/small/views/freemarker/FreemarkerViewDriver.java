/*******************************************************************************
 * Copyright (c) 2020 Black Rook Software
 * This program and the accompanying materials are made available under the 
 * terms of the GNU Lesser Public License v2.1 which accompanies this 
 * distribution, and is available at 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.small.views.freemarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blackrook.small.exception.views.ViewProcessingException;
import com.blackrook.small.roles.ViewDriver;
import com.blackrook.small.util.SmallResponseUtils;
import com.blackrook.small.util.SmallUtils;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * The Freemarker View Driver for Small.
 * @author Matthew Tropiano
 */
public abstract class FreemarkerViewDriver implements ViewDriver
{
	/** The Freemarker configuration. */
	private Configuration configuration;
	/** The forced output MIME-Type. */
	private String mimeType;
	/** Init character capacity. */
	private int capacity;
	
	/**
	 * Creates the Freemarker View Driver for Small.
	 * @param configuration the Freemarker Configuration.
	 */
	public FreemarkerViewDriver(Configuration configuration)
	{
		this.configuration = configuration;
		this.mimeType = null;
		this.capacity = 4096;
	}
	
	/**
	 * Creates the Freemarker View Driver for Small with a specific template loader.
	 * Also turns off "localized lookup" - assumes asked-for view is the actual name.
	 * @param loader the template loader to use.
	 */
	public FreemarkerViewDriver(TemplateLoader loader)
	{
		Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		config.setTemplateLoader(loader);
		config.setLocalizedLookup(false);
		this.configuration = config;
		this.mimeType = null;
	}
	
	/**
	 * Sets the forced MIME-Type.
	 * @param mimeType the forced MIME-type for the output.
	 */
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}
	
	/**
	 * Sets the initial capacity of the output buffer for the view.
	 * @throws IllegalArgumentException if capacity is &lt; 1.
	 */
	public void setCapacity(int capacity)
	{
		if (capacity <= 0)
			throw new IllegalArgumentException("capacity cannot be < 1.");
		this.capacity = capacity;
	}
	
	/**
	 * Creates a new resource-based template loader.
	 * @param resourceRoot the root path for resources.
	 * @return the created template loader.
	 */
	public static TemplateLoader createResourceTemplateLoader(final String resourceRoot)
	{
		final String root = SmallUtils.removeBeginningSlash(SmallUtils.removeEndingSlash(resourceRoot));
		return new TemplateLoader()
		{
			@Override
			public Reader getReader(Object templateSource, String encoding) throws IOException
			{
				return new InputStreamReader((InputStream)templateSource, Charset.forName(encoding));
			}
			
			@Override
			public long getLastModified(Object templateSource)
			{
				return -1;
			}
			
			@Override
			public Object findTemplateSource(String name) throws IOException
			{
				return SmallUtils.openResource(root + SmallUtils.addBeginningSlash(name));
			}
			
			@Override
			public void closeTemplateSource(Object templateSource) throws IOException
			{
				((InputStream)templateSource).close();
			}
		};
	}
	
	/**
	 * Creates a new file-based template loader.
	 * @param fileRootPath the root path for files.
	 * @return the created template loader.
	 * @throws IllegalArgumentException if fileRootPath is not a directory.
	 */
	public static TemplateLoader createFileTemplateLoader(final String fileRootPath)
	{
		return createFileTemplateLoader(new File(fileRootPath));
	}
	
	/**
	 * Creates a new file-based template loader.
	 * @param fileRoot the root path for files.
	 * @return the created template loader.
	 * @throws IllegalArgumentException if fileRoot is not a directory.
	 */
	public static TemplateLoader createFileTemplateLoader(final File fileRoot)
	{
		if (!fileRoot.isDirectory())
			throw new IllegalArgumentException("File root " + fileRoot + " is not a directory."); 
		
		return new TemplateLoader()
		{
			@Override
			public Reader getReader(Object templateSource, String encoding) throws IOException
			{
				return new InputStreamReader(new FileInputStream((File)templateSource), Charset.forName(encoding));
			}
			
			@Override
			public long getLastModified(Object templateSource)
			{
				return ((File)templateSource).lastModified();
			}
			
			@Override
			public Object findTemplateSource(String name) throws IOException
			{
				return new File(fileRoot.getAbsolutePath() + File.separator + SmallUtils.addBeginningSlash(name));
			}
			
			@Override
			public void closeTemplateSource(Object templateSource) throws IOException
			{
				// Not closeable.
			}
		};
	}
	
	/**
	 * Checks if this handler should process a view by its view name.
	 * If false is returned, this view driver is skipped.
	 * @param viewName the name of the view to process.
	 * @return true if so, false if not.
	 */
	protected abstract boolean acceptViewName(String viewName);
	
	@Override
	public boolean handleView(HttpServletRequest request, HttpServletResponse response, Object model, String viewName) throws ViewProcessingException
	{
		if (!acceptViewName(viewName))
			return false;
		try {
			StringWriter sw = new StringWriter(capacity);
			Template template = configuration.getTemplate(viewName);
			template.process(model, sw);
			String mime = mimeType != null ? mimeType : SmallUtils.getMIMEType(request.getServletContext(), viewName);
			SmallResponseUtils.sendStringData(response, mime, sw.toString());
			return true;
		} catch (IOException e) {
			throw new ViewProcessingException("I/O error occurred!", e);
		} catch (TemplateException e) {
			throw new ViewProcessingException("Template error occurred!", e);
		}
	}

}
