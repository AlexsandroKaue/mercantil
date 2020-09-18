package com.kaue.dao.custom.impl;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

public class RegisterPostgresqlDialect implements MetadataBuilderContributor{

	@Override
	public void contribute(MetadataBuilder metadataBuilder) {
		metadataBuilder.applySqlFunction(
	        "regexp_replace",
	        new StandardSQLFunction(
	        	"REGEXP_REPLACE",
	        	StringType.INSTANCE
	        )
	    );
	}

}
