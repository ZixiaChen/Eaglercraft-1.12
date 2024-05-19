package net.minecraft.client.audio;

import java.util.List;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.SoundCategory;

public class SoundList
{
    private final List<Sound> sounds;
    private final List<SoundList.SoundEntry> soundList = Lists.newArrayList();

    /**
     * if true it will override all the sounds from the resourcepacks loaded before
     */
    private final boolean replaceExisting;
    private final String subtitle;
    private SoundCategory category;

    public SoundList(List<Sound> soundsIn, boolean replceIn, String subtitleIn)
    {
        this.sounds = soundsIn;
        this.replaceExisting = replceIn;
        this.subtitle = subtitleIn;
    }

    public List<Sound> getSounds()
    {
        return this.sounds;
    }

    public List<SoundList.SoundEntry> getSoundList() {
		return this.soundList;
	}

    public boolean canReplaceExisting()
    {
        return this.replaceExisting;
    }

    @Nullable
    public String getSubtitle()
    {
        return this.subtitle;
    }

    public SoundCategory getSoundCategory()
    {
        return this.category;
    }

    public void setSoundCategory(SoundCategory soundCat)
    {
        this.category = soundCat;
    }

    public static class SoundEntry {
		private String name;
		private float volume = 1.0F;
		private float pitch = 1.0F;
		private int weight = 1;
		private SoundList.SoundEntry.Type type = SoundList.SoundEntry.Type.FILE;
		private boolean streaming = false;

		public String getSoundEntryName() {
			return this.name;
		}

		public void setSoundEntryName(String nameIn) {
			this.name = nameIn;
		}

		public float getSoundEntryVolume() {
			return this.volume;
		}

		public void setSoundEntryVolume(float volumeIn) {
			this.volume = volumeIn;
		}

		public float getSoundEntryPitch() {
			return this.pitch;
		}

		public void setSoundEntryPitch(float pitchIn) {
			this.pitch = pitchIn;
		}

		public int getSoundEntryWeight() {
			return this.weight;
		}

		public void setSoundEntryWeight(int weightIn) {
			this.weight = weightIn;
		}

		public SoundList.SoundEntry.Type getSoundEntryType() {
			return this.type;
		}

		public void setSoundEntryType(SoundList.SoundEntry.Type typeIn) {
			this.type = typeIn;
		}

		public boolean isStreaming() {
			return this.streaming;
		}

		public void setStreaming(boolean isStreaming) {
			this.streaming = isStreaming;
		}

		public static enum Type {
			FILE("file"), SOUND_EVENT("event");

			private final String field_148583_c;

			private Type(String parString2) {
				this.field_148583_c = parString2;
			}

			public static SoundList.SoundEntry.Type getType(String parString1) {
				for (SoundList.SoundEntry.Type soundlist$soundentry$type : values()) {
					if (soundlist$soundentry$type.field_148583_c.equals(parString1)) {
						return soundlist$soundentry$type;
					}
				}

				return null;
			}
		}
	}
}
