package pt.ipleiria.estg.dei.ei.dae.pmei.util;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;

import java.util.Comparator;

public class EventoComparator implements Comparator<Evento> {
    @Override
    public int compare(Evento e1, Evento e2) {
        return e2.getTimestamp().compareTo(e1.getTimestamp());
    }
}
