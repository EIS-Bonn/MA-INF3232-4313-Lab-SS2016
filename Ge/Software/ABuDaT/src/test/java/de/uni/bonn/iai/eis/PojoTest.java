package de.uni.bonn.iai.eis;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoField;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.Rule;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.component.PipelineElement;
import de.uni.bonn.iai.eis.etl.linkedpipes.EmptyPipelineResponseEntity;
import de.uni.bonn.iai.eis.web.model.*;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PojoTest {

    private class SetterMustExistRuleWithFilter implements Rule {

        private List<String> fieldsToExclude;

        SetterMustExistRuleWithFilter(List<String> fieldsToExclude) {
            this.fieldsToExclude = fieldsToExclude;
        }

        public void evaluate(final PojoClass pojoClass) {
            for (PojoField fieldEntry : pojoClass.getPojoFields()) {
                if (!fieldsToExclude.contains(fieldEntry.getName())) {
                    if (!fieldEntry.isFinal() && !fieldEntry.hasSetter()) {
                        Affirm.fail(String.format("[%s] is missing a setter", fieldEntry));
                    }
                }
            }
        }
    }


    @Test
    public void testPojos() {
        Validator defaultValidator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        List<Class> pojoClassesToTest = new ArrayList<>();

        pojoClassesToTest.add(Pipeline.class);
        pojoClassesToTest.add(PipelineElement.class);
        pojoClassesToTest.add(EmptyPipelineResponseEntity.class);
        pojoClassesToTest.add(ChooseInput.class);
        pojoClassesToTest.add(SparqlUpdate.class);
        pojoClassesToTest.add(Mapping.class);
        pojoClassesToTest.add(CodelistTransformation.class);
        pojoClassesToTest.add(DataTransformation.class);
        pojoClassesToTest.add(CustomDimension.class);
        pojoClassesToTest.add(CustomMeasure.class);


        for (Class clazz : pojoClassesToTest) {
            PojoClass pojo = PojoClassFactory.getPojoClass(clazz);
            defaultValidator.validate(pojo);
        }
    }


    /**
     * Test for db entities, db ids should only be set by the db engine, therefor filter them in the test as they dont have setters
     */
    @Test
    public void testEntitiyPojo() {
        List<String> fieldsToExclude = new ArrayList<>();
        fieldsToExclude.add("id");

        Validator validatorWithFilter = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRuleWithFilter(fieldsToExclude))
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        List<Class> pojoClassesToTest = new ArrayList<>();

        pojoClassesToTest.add(DataTransformation.class);

        for (Class clazz : pojoClassesToTest) {
            PojoClass pojo = PojoClassFactory.getPojoClass(clazz);
            validatorWithFilter.validate(pojo);
        }

    }
}