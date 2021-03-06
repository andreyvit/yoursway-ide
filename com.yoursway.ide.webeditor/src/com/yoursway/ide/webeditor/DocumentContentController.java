package com.yoursway.ide.webeditor;

import static com.yoursway.utils.YsFileUtils.readAsString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.yoursway.ide.application.controllers.mainwindow.EditorComponent;
import com.yoursway.ide.application.model.Document;

public class DocumentContentController implements DocumentContentWindowCallback,
		EditorComponent {

	private DocumentContentWindow view;
	private final Document document;
	private DocumentContentWindowModelImpl viewModel;

	public DocumentContentController(Document document,
			DocumentContentWindowFactory factory) {
		if (document == null)
			throw new NullPointerException("document is null");
		this.document = document;
		this.viewModel = new DocumentContentWindowModelImpl();
		this.view = factory.bind(this.viewModel, this);
		File file = document.file();
		viewModel.file().setValue(file);
		try {
			viewModel.data().setValue(readAsString(file));
		} catch (FileNotFoundException e) {
			viewModel.data().setValue("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
