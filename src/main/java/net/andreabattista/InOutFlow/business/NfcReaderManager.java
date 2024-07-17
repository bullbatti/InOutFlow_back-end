package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.NfcReaderDao;
import net.andreabattista.InOutFlow.model.NfcReader;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class NfcReaderManager {

    private static final Faker FAKER = new Faker();

    public static List<NfcReader> createFake() {
        NfcReaderDao nfcReaderDao = new NfcReaderDao();

        List<NfcReader> nfcReaders = new ArrayList<>();

        for (int i = 0; i < 2; ++i) {
            NfcReader nfcReader = new NfcReader();
            nfcReader.setPosition(FAKER.name().title());

            if (i == 0) {
                nfcReader.setNfcReaderType(NfcReader.Type.ENTRY);
            } else {
                nfcReader.setNfcReaderType(NfcReader.Type.EXIT);
            }

            nfcReaderDao.save(nfcReader);
            nfcReaders.add(nfcReader);
        }

        return nfcReaders;
    }
}
