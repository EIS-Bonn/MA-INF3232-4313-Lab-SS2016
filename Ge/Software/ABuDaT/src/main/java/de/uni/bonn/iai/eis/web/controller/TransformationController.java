package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.web.model.AbstractTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import java.net.ConnectException;

public abstract class TransformationController<T extends AbstractTransformation> {

    private static final Logger log = LoggerFactory.getLogger(TransformationController.class);

    void tryExecute(T transformation, Model model) {
        try {
            execute(transformation);
            model.addAttribute("success", "The pipeline was successfully sent to linkedpipes");
        } catch (Exception e) {
            if (e.getCause() instanceof ConnectException) {
                log.error("Error connecting to linkedpipes ETL.", e);
                model.addAttribute("error", "Error connecting to linkedpipes ETL... Is it running?");
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    abstract void execute(T transformation) throws ConnectException;
}