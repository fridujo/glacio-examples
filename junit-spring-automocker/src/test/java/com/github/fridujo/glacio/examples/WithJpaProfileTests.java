package com.github.fridujo.glacio.examples;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.test.context.ActiveProfiles;

import com.github.fridujo.automocker.base.Automocker;
import com.github.fridujo.glacio.extension.spring.SpringExtension;
import com.github.fridujo.glacio.running.api.configuration.GlacioConfiguration;
import com.github.fridujo.glacio.running.api.extension.ExtendGlacioWith;

@GlacioConfiguration(
    featurePaths = "classpath:features",
    gluePaths = "com.github.fridujo.glacio.examples"
)
@ExtendGlacioWith(SpringExtension.class)
@SpringBootTest(classes = PetClinicApplication.class, properties = "spring.main.banner-mode=off")
@ActiveProfiles("jpa")
@Automocker
public class WithJpaProfileTests {
}
