'use client';

import { useState } from 'react';
import { useFieldArray, useForm } from 'react-hook-form';
import { toast } from 'sonner';
import { CreateTitleRequest } from '@/types/title';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { AlertCircle, Plus, Trash2 } from 'lucide-react';

interface TitleFormData {
  name: string;
  titleInfoUrls: Array<{ value: string }>;
}

interface TitleFormProps {
  onSubmit: (data: CreateTitleRequest) => Promise<void>;
  isLoading?: boolean;
}

export default function TitleForm({ onSubmit, isLoading = false }: TitleFormProps) {
  const [submitError, setSubmitError] = useState<string | null>(null);

  const form = useForm<TitleFormData>({
    defaultValues: {
      name: '',
      titleInfoUrls: [{ value: '' }],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: 'titleInfoUrls',
  });

  const handleFormSubmit = async (values: TitleFormData) => {
    setSubmitError(null);
    try {
      // Validate name
      if (!values.name.trim()) {
        setSubmitError('タイトル名は必須です');
        return;
      }

      // Extract and filter URLs
      const filledUrls = values.titleInfoUrls
        .map(item => item.value.trim())
        .filter(url => url.length > 0);

      await onSubmit({
        name: values.name,
        titleInfoUrls: filledUrls.length > 0 ? filledUrls : undefined,
      });
      toast.success('タイトルを作成しました');
      form.reset();
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'エラーが発生しました';
      setSubmitError(errorMessage);
      toast.error(errorMessage);
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(handleFormSubmit)} className="space-y-6">
        {submitError && (
          <Alert variant="destructive">
            <AlertCircle className="h-4 w-4" />
            <AlertDescription>{submitError}</AlertDescription>
          </Alert>
        )}

        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>
                タイトル名 <span className="text-danger-500">*</span>
              </FormLabel>
              <FormControl>
                <Input
                  placeholder="例: 進撃の巨人"
                  disabled={isLoading}
                  {...field}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <div>
          <Label className="mb-3 block">タイトル情報URL</Label>
          <div className="space-y-3">
            {fields.map((field, index) => (
              <FormField
                key={field.id}
                control={form.control}
                name={`titleInfoUrls.${index}.value`}
                render={({ field: urlField }) => (
                  <FormItem>
                    <FormControl>
                      <div className="flex gap-3 items-end">
                        <Input
                          type="url"
                          placeholder="https://..."
                          disabled={isLoading}
                          className="flex-1"
                          {...urlField}
                        />
                        {fields.length > 1 && (
                          <Button
                            type="button"
                            variant="destructive"
                            size="sm"
                            onClick={() => remove(index)}
                            disabled={isLoading}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        )}
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            ))}
          </div>
          <Button
            type="button"
            variant="outline"
            size="sm"
            onClick={() => append({ value: '' })}
            disabled={isLoading}
            className="mt-3"
          >
            <Plus className="mr-2 h-4 w-4" />
            URL を追加
          </Button>
        </div>

        <Button type="submit" disabled={isLoading || form.formState.isSubmitting}>
          {isLoading || form.formState.isSubmitting ? '作成中...' : '作成'}
        </Button>
      </form>
    </Form>
  );
}
